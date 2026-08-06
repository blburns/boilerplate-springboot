package com.boilerplate.springboot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class ItemApiIntegrationTest {

  @BeforeAll
  static void requireDocker() {
    assumeTrue(
        DockerClientFactory.instance().isDockerAvailable(),
        "Docker is required for Testcontainers Postgres tests");
  }

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

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
