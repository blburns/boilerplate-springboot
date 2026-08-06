package com.boilerplate.springboot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.DriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItemApiIntegrationTest {

  @BeforeAll
  static void requirePostgres() {
    String url =
        System.getenv()
            .getOrDefault(
                "SPRING_DATASOURCE_URL",
                "jdbc:postgresql://"
                    + System.getenv().getOrDefault("DB_HOST", "localhost")
                    + ":"
                    + System.getenv().getOrDefault("DB_PORT", "5432")
                    + "/"
                    + System.getenv().getOrDefault("DB_NAME", "boilerplate"));
    String user = System.getenv().getOrDefault("DB_USER", "boilerplate");
    String password = System.getenv().getOrDefault("DB_PASSWORD", "boilerplate");
    try {
      DriverManager.getConnection(url, user, password).close();
    } catch (Exception ex) {
      assumeTrue(
          false,
          "Postgres required for integration tests ("
              + user
              + "@"
              + url
              + "). Run: ./scripts/manage.sh db-init");
    }
  }

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser
  void createGetAndDeleteItem() throws Exception {
    String location =
        mockMvc
            .perform(
                post("/api/v1/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {"name":"Widget","description":"Demo item"}
                        """))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value("Widget"))
            .andReturn()
            .getResponse()
            .getHeader("Location");

    assertThat(location).isNotBlank();

    mockMvc
        .perform(get(location))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.description").value("Demo item"));

    String id = location.substring(location.lastIndexOf('/') + 1);
    mockMvc.perform(delete("/api/v1/items/" + id)).andExpect(status().isNoContent());
    mockMvc.perform(get("/api/v1/items/" + id)).andExpect(status().isNotFound());
  }
}
