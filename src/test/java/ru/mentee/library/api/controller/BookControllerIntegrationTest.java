package ru.mentee.library.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class BookControllerIntegrationTest {

  @Container static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add(
        "spring.liquibase.change-log", () -> "classpath:db/changelog/db.changelog-master.yaml");
  }

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("Should создать книгу с валидными данными")
  void shouldCreateBookWithValidData() throws Exception {
    String requestJson =
        """
            {
                "title": "Test Book",
                "author": "Test Author",
                "isbn": "978-3-16-148410-0"
            }
            """;

    mockMvc
        .perform(post("/api/v1/books").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }

  @Test
  @DisplayName("Should получить книгу по ID")
  void shouldGetBookById() throws Exception {

    String createJson =
        """
            {
                "title": "Test Book",
                "author": "Test Author",
                "isbn": "978-3-16-148410-0"
            }
            """;

    mockMvc
        .perform(post("/api/v1/books").contentType(MediaType.APPLICATION_JSON).content(createJson))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Long bookId = 1L;
    mockMvc
        .perform(get("/api/v1/books/" + bookId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookId))
        .andExpect(jsonPath("$.title").value("Test Book"))
        .andExpect(jsonPath("$.author").value("Test Author"));
  }
}
