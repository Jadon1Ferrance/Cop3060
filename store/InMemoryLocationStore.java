package edu.famu.cop3060.resources.store;

import edu.famu.cop3060.resources.dto.LocationDTO;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryLocationStore {
  private final Map<Long, LocationDTO> byId = new HashMap<>();
  private final AtomicLong seq = new AtomicLong(100);

  public InMemoryLocationStore() {
    // seed a few
    save(new LocationDTO(null, "Computer Lab", "SB", "120"));
    save(new LocationDTO(null, "Library", "LIB", "1F"));
    save(new LocationDTO(null, "Advising", "STU", "210"));
  }

  public List<LocationDTO> findAll() { return List.copyOf(byId.values()); }
  public Optional<LocationDTO> findById(Long id) { return Optional.ofNullable(byId.get(id)); }

  public LocationDTO save(LocationDTO in) {
    Long id = seq.incrementAndGet();
    LocationDTO dto = new LocationDTO(id, in.name(), in.building(), in.room());
    byId.put(id, dto);
    return dto;
  }

  public LocationDTO update(Long id, LocationDTO in) {
    if (!byId.containsKey(id)) return null;
    LocationDTO dto = new LocationDTO(id, in.name(), in.building(), in.room());
    byId.put(id, dto);
    return dto;
  }

  public boolean delete(Long id) { return byId.remove(id) != null; }
}
