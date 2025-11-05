package edu.famu.cop3060.resources.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

/**
 * Response DTO expands nested related DTOs (category/location).
 */
public record ResourceDTO(
    Long id,
    @NotBlank String name,
    @Pattern(regexp="https?://.*", message="url must start with http or https")
    String url,
    List<String> tags,
    @NotNull Long locationId,
    @NotNull Long categoryId,
    LocationDTO location,    // expanded
    CategoryDTO category     // expanded
) {}
