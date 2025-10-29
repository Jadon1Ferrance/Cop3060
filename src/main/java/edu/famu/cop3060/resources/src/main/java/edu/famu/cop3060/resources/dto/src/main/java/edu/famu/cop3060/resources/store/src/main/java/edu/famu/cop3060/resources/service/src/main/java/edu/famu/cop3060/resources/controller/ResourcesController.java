package edu.famu.cop3060.resources.controller;

import edu.famu.cop3060.resources.dto.ResourceDTO;
import edu.famu.cop3060.resources.service.ResourcesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resources")
public class ResourcesController {
  private static final Logger log = LoggerFactory.getLogger(ResourcesController.class);
  private final ResourcesService service;

  public ResourcesController(ResourcesService service) { this.service = service; }

  // GET /api/resources?category=Tutoring&q=java
  @GetMapping
  public List<ResourceDTO> list(@RequestParam Optional<String> category,
                               @RequestParam Optional<String> q) {
    List<ResourceDTO> out = service.list(category, q);
    log.info("GET /api/resources category={} q={} -> {} result(s)", category.orElse("-"), q.orElse("-"), out.size());
    return out;
  }

  // GET /api/resources/{id}
  @GetMapping("/{id}")
  public ResponseEntity<ResourceDTO> detail(@PathVariable String id) {
    return service.get(id)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
