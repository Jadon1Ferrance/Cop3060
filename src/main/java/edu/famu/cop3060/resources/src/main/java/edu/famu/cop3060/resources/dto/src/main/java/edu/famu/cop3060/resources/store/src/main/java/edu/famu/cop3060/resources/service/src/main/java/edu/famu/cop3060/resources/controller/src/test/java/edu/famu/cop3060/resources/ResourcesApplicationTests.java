package edu.famu.cop3060.resources;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ResourcesApplicationTests {

  @Autowired MockMvc mvc;

  @Test
  void list_returnsOk_andHasItems() throws Exception {
    mvc.perform(get("/api/resources"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(Matchers.greaterThanOrEqualTo(5)));
  }

  @Test
  void detail_returnsOk_andHasName() throws Exception {
    mvc.perform(get("/api/resources/tutor-101"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").exists());
  }

  @Test
  void detail_notFound_forUnknownId() throws Exception {
    mvc.perform(get("/api/resources/does-not-exist"))
      .andExpect(status().isNotFound());
  }
}
