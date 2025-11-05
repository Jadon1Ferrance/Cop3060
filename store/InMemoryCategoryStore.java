package edu.famu.cop3060.resources.store;

import edu.famu.cop3060.resources.dto.CategoryDTO;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCategoryStore {
  private final Map<Long, CategoryDTO> byId = new HashMap<>();
  private final AtomicLong seq = new AtomicLong(200);

  public InMemoryCategoryStore() {
    save(new CategoryDTO(null, "Tutoring", "Course help"));
    save(new CategoryDTO(null, "Advising", "Academic planning"));
    save(new CategoryDTO(null, "Lab", "Hands-on practice"));
  }

  public List<CategoryDTO> findAll() { return List.copyOf(byId.values()); }
  public Optional<CategoryDTO> findById(Long id) { return Optional.ofNullable(byId.get(id)); }

  public CategoryDTO save(CategoryDTO in) {
    Long id = seq.incrementAndGet();
    CategoryDTO dto = new CategoryDTO(id, in.name(), in.description());
    byId.put(id, dto);
    return dto;
  }

  public CategoryDTO update(Long id, CategoryDTO in) {
    if (!byId.containsKey(id)) return null;
    CategoryDTO dto = new CategoryDTO(id, in.name(), in.description());
    byId.put(id, dto);
    return dto;
  }

  public boolean delete(Long id) { return byId.remove(id) != null; }
}
