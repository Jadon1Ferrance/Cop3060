package edu.famu.cop3060.resources.store;

import edu.famu.cop3060.resources.dto.ResourceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryResourceStore {
  private static final Logger log = LoggerFactory.getLogger(InMemoryResourceStore.class);

  private final Map<String, ResourceDTO> byId = new HashMap<>();
  private final List<ResourceDTO> all = new ArrayList<>();

  public InMemoryResourceStore() {
    seed();
    log.info("Seeded {} campus resources", all.size());
  }

  private void add(ResourceDTO r) {
    all.add(r);
    byId.put(r.id(), r);
  }

  private void seed() {
    add(new ResourceDTO("tutor-101", "CS Tutoring Center", "Tutoring", "Engineering Bldg 201", "https://example.edu/tutor/cs", List.of("java", "cs", "algorithms")));
    add(new ResourceDTO("lab-201", "Data Science Lab", "Lab", "Science Hall 3F", "https://example.edu/labs/ds", List.of("python", "ml")));
    add(new ResourceDTO("advising-1", "Advising Office", "Advising", "Student Center 120", "https://example.edu/advising", List.of("scheduling", "degree")));
    add(new ResourceDTO("library-1", "Main Library", "Library", "Library West", "https://example.edu/library", List.of("study", "books")));
    add(new ResourceDTO("career-1", "Career Services", "Career", "Student Center 220", "https://example.edu/career", List.of("resume", "internships")));
  }

  public List<ResourceDTO> findAll() { return List.copyOf(all); }
  public Optional<ResourceDTO> findById(String id) { return Optional.ofNullable(byId.get(id)); }

  public List<ResourceDTO> findByFilters(Optional<String> category, Optional<String> q) {
    return all.stream()
      .filter(r -> category.map(c -> r.category() != null && r.category().equalsIgnoreCase(c)).orElse(true))
      .filter(r -> q.map(s -> {
        String needle = s.toLowerCase();
        boolean inName = r.name() != null && r.name().toLowerCase().contains(needle);
        boolean inTags = r.tags() != null && r.tags().stream().anyMatch(t -> t.toLowerCase().contains(needle));
        return inName || inTags;
      }).orElse(true))
      .toList();
  }
}

