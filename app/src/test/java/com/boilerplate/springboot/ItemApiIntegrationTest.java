package com.boilerplate.springboot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
          false, "Postgres required for integration tests. Run: ./scripts/manage.sh db-init");
    }
  }

  @Autowired private MockMvc mockMvc;

  @Test
  void createGetAndDeleteItem() throws Exception {
    var admin =
        user("admin@example.com")
            .authorities(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("items:read"),
                new SimpleGrantedAuthority("items:write"),
                new SimpleGrantedAuthority("admin:access"));

    String location =
        mockMvc
            .perform(
                post("/api/v1/items")
                    .with(admin)
                    .with(csrf())
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
        .perform(get(location).with(admin))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.description").value("Demo item"));

    String id = location.substring(location.lastIndexOf('/') + 1);
    mockMvc
        .perform(delete("/api/v1/items/" + id).with(admin).with(csrf()))
        .andExpect(status().isNoContent());
    mockMvc.perform(get("/api/v1/items/" + id).with(admin)).andExpect(status().isNotFound());
  }
}
