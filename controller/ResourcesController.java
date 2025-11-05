package edu.famu.cop3060.resources.controller;

import edu.famu.cop3060.resources.dto.*;
import edu.famu.cop3060.resources.service.ResourcesService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/resources")
public class ResourcesController {
  private static final Logger log = LoggerFactory.getLogger(ResourcesController.class);
  private final ResourcesService svc;

  public ResourcesController(ResourcesService svc) { this.svc = svc; }

  @GetMapping
  public ResponseEntity<PageResponse<ResourceDTO>> list(
      @RequestParam Optional<String> category,
      @RequestParam Optional<String> q,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String sort
  ) {
    log.info("GET /api/resources?page={}&size={}&sort={}&category={}&q={}", page, size, sort, category.orElse(null), q.orElse(null));
    return ResponseEntity.ok(svc.listResources(category, q, page, size, sort));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResourceDTO> one(@PathVariable Long id) {
    log.info("GET /api/resources/{}", id);
    return ResponseEntity.ok(svc.findOne(id));
  }

  @PostMapping
  public ResponseEntity<ResourceDTO> create(@Valid @RequestBody ResourceCreateDTO in) {
    log.info("POST /api/resources");
    ResourceDTO out = svc.create(in);
    return ResponseEntity.status(201).body(out);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResourceDTO> update(@PathVariable Long id, @Valid @RequestBody ResourceUpdateDTO in) {
    log.info("PUT /api/resources/{}", id);
    return ResponseEntity.ok(svc.update(id, in));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info("DELETE /api/resources/{}", id);
    svc.delete(id);
    return ResponseEntity.noContent().build();
  }
}
