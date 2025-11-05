package edu.famu.cop3060.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.famu.cop3060.resources.dto.LocationDTO;
import edu.famu.cop3060.resources.dto.ResourceCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ResourcesApiTests {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;

  @Test
  void listLocations_ok() throws Exception {
    mvc.perform(get("/api/locations"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))));
  }

  @Test
  void listCategories_ok() throws Exception {
    mvc.perform(get("/api/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))));
  }

  @Test
  void getResource_404_whenMissing() throws Exception {
    mvc.perform(get("/api/resources/99999")).andExpect(status().isNotFound());
  }

  @Test
  void createLocation_thenGet() throws Exception {
    var body = new LocationDTO(null, "Engineering", "ENG", "301");
    mvc.perform(post("/api/locations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists());
  }

  @Test
  void createResource_andList() throws Exception {
    // use existing seeded ids: 
    long locId = 101L; // from store seed (first created = 101)
    long catId = 201L; // from store seed (first created = 201)

    var body = new ResourceCreateDTO(
        "Math Tutoring",
        "https://example.com/tutor",
        List.of("math","help"),
        locId,
        catId
    );

    mvc.perform(post("/api/resources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.location.id", is((int)locId)))
        .andExpect(jsonPath("$.category.id", is((int)catId)));

    mvc.perform(get("/api/resources"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))));
  }

  @Test
  void deleteCategory_inUse_is409() throws Exception {
    long locId = 101L;
    long catId = 201L;

    // create a resource that references catId
    var body = new ResourceCreateDTO(
        "CS Help",
        "https://example.com/cs",
        List.of("cs"),
        locId,
        catId
    );
    mvc.perform(post("/api/resources")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(body)))
      .andExpect(status().isCreated());

    mvc.perform(delete("/api/categories/{id}", catId))
        .andExpect(status().isConflict());
  }
}
