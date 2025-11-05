package edu.famu.cop3060.resources.service;

import edu.famu.cop3060.resources.dto.*;
import edu.famu.cop3060.resources.exception.InvalidReferenceException;
import edu.famu.cop3060.resources.exception.NotFoundException;
import edu.famu.cop3060.resources.store.InMemoryCategoryStore;
import edu.famu.cop3060.resources.store.InMemoryLocationStore;
import edu.famu.cop3060.resources.store.InMemoryResourceStore;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResourcesService {
  private final InMemoryResourceStore resources = new InMemoryResourceStore();
  private final InMemoryLocationStore locations = new InMemoryLocationStore();
  private final InMemoryCategoryStore categories = new InMemoryCategoryStore();

  // ----- helpers -----
  private <T> PageResponse<T> page(List<T> items, int page, int size, String sort) {
    if (size <= 0) size = 10;
    if (page < 0) page = 0;
    int from = Math.min(page * size, items.size());
    int to = Math.min(from + size, items.size());
    List<T> content = items.subList(from, to);
    int totalPages = (int)Math.ceil(items.size() / (double)size);
    return new PageResponse<>(content, page, size, items.size(), totalPages, sort);
  }

  private Comparator<ResourceDTO> comparatorFor(String sort) {
    boolean desc = sort != null && sort.startsWith("-");
    String field = (sort == null)? "name" : sort.replace("-", "");
    Comparator<ResourceDTO> c = switch (field) {
      case "url" -> Comparator.comparing(r -> Optional.ofNullable(r.url()).orElse(""), String::compareToIgnoreCase);
      case "category" -> Comparator.comparing(r -> Optional.ofNullable(r.category().name()).orElse(""), String::compareToIgnoreCase);
      default -> Comparator.comparing(r -> Optional.ofNullable(r.name()).orElse(""), String::compareToIgnoreCase);
    };
    return desc ? c.reversed() : c;
  }

  private ResourceDTO expand(ResourceDTO r) {
    LocationDTO loc = locations.findById(r.locationId()).orElse(null);
    CategoryDTO cat = categories.findById(r.categoryId()).orElse(null);
    return new ResourceDTO(r.id(), r.name(), r.url(), r.tags(), r.locationId(), r.categoryId(), loc, cat);
  }

  // ----- related listing with paging/sort -----
  public PageResponse<LocationDTO> listLocations(int page, int size, String sort) {
    Comparator<LocationDTO> cmp = (sort != null && sort.startsWith("-"))
        ? Comparator.comparing(LocationDTO::name, String::compareToIgnoreCase).reversed()
        : Comparator.comparing(LocationDTO::name, String::compareToIgnoreCase);
    List<LocationDTO> all = locations.findAll().stream().sorted(cmp).toList();
    return page(all, page, size, sort);
  }

  public PageResponse<CategoryDTO> listCategories(int page, int size, String sort) {
    Comparator<CategoryDTO> cmp = (sort != null && sort.startsWith("-"))
        ? Comparator.comparing(CategoryDTO::name, String::compareToIgnoreCase).reversed()
        : Comparator.comparing(CategoryDTO::name, String::compareToIgnoreCase);
    List<CategoryDTO> all = categories.findAll().stream().sorted(cmp).toList();
    return page(all, page, size, sort);
  }

  // ----- related CRUD -----
  public CategoryDTO createCategory(CategoryDTO in) {
    return categories.save(new CategoryDTO(null, in.name(), in.description()));
  }
  public CategoryDTO updateCategory(Long id, CategoryDTO in) {
    CategoryDTO out = categories.update(id, in);
    if (out == null) throw new NotFoundException("Category " + id + " not found");
    return out;
  }
  public void deleteCategory(Long id) {
    long used = resources.countByCategory(id);
    if (used > 0) throw new IllegalStateException("Category " + id + " in use by " + used + " resource(s)");
    if (!categories.delete(id)) throw new NotFoundException("Category " + id + " not found");
  }

  public LocationDTO createLocation(LocationDTO in) {
    return locations.save(new LocationDTO(null, in.name(), in.building(), in.room()));
  }
  public LocationDTO updateLocation(Long id, LocationDTO in) {
    LocationDTO out = locations.update(id, in);
    if (out == null) throw new NotFoundException("Location " + id + " not found");
    return out;
  }
  public void deleteLocation(Long id) {
    long used = resources.countByLocation(id);
    if (used > 0) throw new IllegalStateException("Location " + id + " in use by " + used + " resource(s)");
    if (!locations.delete(id)) throw new NotFoundException("Location " + id + " not found");
  }

  public LocationDTO getLocation(Long id) {
    return locations.findById(id).orElseThrow(() -> new NotFoundException("Location " + id + " not found"));
  }
  public CategoryDTO getCategory(Long id) {
    return categories.findById(id).orElseThrow(() -> new NotFoundException("Category " + id + " not found"));
  }

  // ----- resources -----
  public PageResponse<ResourceDTO> listResources(Optional<String> categoryFilter, Optional<String> q,
                                                 int page, int size, String sort) {

    Map<Long, CategoryDTO> catMap = categories.findAll().stream()
        .collect(Collectors.toMap(CategoryDTO::id, Function.identity()));

    List<ResourceDTO> allExpanded = resources.findAll().stream()
        .map(this::expand)
        .toList();

    List<ResourceDTO> filtered = new ArrayList<>(allExpanded);

    categoryFilter.ifPresent(catName -> {
      String needle = catName.toLowerCase();
      filtered.removeIf(r -> r.category() == null || !r.category().name().toLowerCase().equals(needle));
    });

    q.ifPresent(qs -> {
      String n = qs.toLowerCase();
      filtered.removeIf(r -> {
        boolean inName = Optional.ofNullable(r.name()).orElse("").toLowerCase().contains(n);
        boolean inTags = Optional.ofNullable(r.tags()).orElse(List.of()).stream().anyMatch(t -> t.toLowerCase().contains(n));
        return !(inName || inTags);
      });
    });

    filtered.sort(comparatorFor(sort));
    return page(filtered, page, size, sort);
  }

  public ResourceDTO findOne(Long id) {
    return resources.findById(id).map(this::expand)
        .orElseThrow(() -> new NotFoundException("Resource " + id + " not found"));
  }

  private void verifyRefs(Long locationId, Long categoryId) {
    if (locations.findById(locationId).isEmpty())
      throw new InvalidReferenceException("Invalid locationId: " + locationId);
    if (categories.findById(categoryId).isEmpty())
      throw new InvalidReferenceException("Invalid categoryId: " + categoryId);
  }

  public ResourceDTO create(ResourceCreateDTO in) {
    verifyRefs(in.locationId(), in.categoryId());
    ResourceDTO raw = new ResourceDTO(null, in.name(), in.url(), in.tags(), in.locationId(), in.categoryId(), null, null);
    return expand(resources.save(raw));
  }

  public ResourceDTO update(Long id, ResourceUpdateDTO in) {
    verifyRefs(in.locationId(), in.categoryId());
    ResourceDTO raw = new ResourceDTO(id, in.name(), in.url(), in.tags(), in.locationId(), in.categoryId(), null, null);
    ResourceDTO out = resources.update(id, raw);
    if (out == null) throw new NotFoundException("Resource " + id + " not found");
    return expand(out);
  }

  public void delete(Long id) {
    if (!resources.delete(id)) throw new NotFoundException("Resource " + id + " not found");
  }
}
