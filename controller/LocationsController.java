package edu.famu.cop3060.resources.controller;

import edu.famu.cop3060.resources.dto.LocationDTO;
import edu.famu.cop3060.resources.dto.PageResponse;
import edu.famu.cop3060.resources.service.ResourcesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
public class LocationsController {
  private final ResourcesService svc;
  public LocationsController(ResourcesService svc) { this.svc = svc; }

  @GetMapping
  public ResponseEntity<PageResponse<LocationDTO>> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String sort
  ) {
    return ResponseEntity.ok(svc.listLocations(page, size, sort));
  }

  @GetMapping("/{id}")
  public ResponseEntity<LocationDTO> one(@PathVariable Long id) {
    return ResponseEntity.ok(svc.getLocation(id));
  }

  @PostMapping
  public ResponseEntity<LocationDTO> create(@Valid @RequestBody LocationDTO in) {
    return ResponseEntity.status(201).body(svc.createLocation(in));
  }

  @PutMapping("/{id}")
  public ResponseEntity<LocationDTO> update(@PathVariable Long id, @Valid @RequestBody LocationDTO in) {
    return ResponseEntity.ok(svc.updateLocation(id, in));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    svc.deleteLocation(id);
    return ResponseEntity.noContent().build();
  }
}
