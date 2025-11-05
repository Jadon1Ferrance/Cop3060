package edu.famu.cop3060.resources.controller;

import edu.famu.cop3060.resources.dto.CategoryDTO;
import edu.famu.cop3060.resources.dto.PageResponse;
import edu.famu.cop3060.resources.service.ResourcesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
  private final ResourcesService svc;
  public CategoriesController(ResourcesService svc) { this.svc = svc; }

  @GetMapping
  public ResponseEntity<PageResponse<CategoryDTO>> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String sort
  ) {
    return ResponseEntity.ok(svc.listCategories(page, size, sort));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> one(@PathVariable Long id) {
    return ResponseEntity.ok(svc.getCategory(id));
  }

  @PostMapping
  public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO in) {
    return ResponseEntity.status(201).body(svc.createCategory(in));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO in) {
    return ResponseEntity.ok(svc.updateCategory(id, in));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    svc.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}
