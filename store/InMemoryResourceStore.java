package edu.famu.cop3060.resources.store;

import edu.famu.cop3060.resources.dto.ResourceDTO;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryResourceStore {
  private final Map<Long, ResourceDTO> byId = new HashMap<>();
  private final AtomicLong seq = new AtomicLong(1);

  public List<ResourceDTO> findAll() { return List.copyOf(byId.values()); }
  public Optional<ResourceDTO> findById(Long id) { return Optional.ofNullable(byId.get(id)); }

  public ResourceDTO save(ResourceDTO in) {
    Long id = seq.incrementAndGet();
    ResourceDTO dto = new ResourceDTO(
        id, in.name(), in.url(), in.tags(),
        in.locationId(), in.categoryId(), in.location(), in.category()
    );
    byId.put(id, dto);
    return dto;
  }

  public ResourceDTO update(Long id, ResourceDTO in) {
    if (!byId.containsKey(id)) return null;
    ResourceDTO dto = new ResourceDTO(
        id, in.name(), in.url(), in.tags(),
        in.locationId(), in.categoryId(), in.location(), in.category()
    );
    byId.put(id, dto);
    return dto;
  }

  public boolean delete(Long id) { return byId.remove(id) != null; }

  public long countByLocation(Long locationId) {
    return byId.values().stream().filter(r -> Objects.equals(r.locationId(), locationId)).count();
  }

  public long countByCategory(Long categoryId) {
    return byId.values().stream().filter(r -> Objects.equals(r.categoryId(), categoryId)).count();
  }
}
