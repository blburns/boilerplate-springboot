package com.boilerplate.springboot;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.sql.DriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationContextTest {

  @BeforeAll
  static void requirePostgres() {
    String url =
        "jdbc:postgresql://"
            + System.getenv().getOrDefault("DB_HOST", "localhost")
            + ":"
            + System.getenv().getOrDefault("DB_PORT", "5432")
            + "/"
            + System.getenv().getOrDefault("DB_NAME", "boilerplate");
    String user = System.getenv().getOrDefault("DB_USER", "boilerplate");
    String password = System.getenv().getOrDefault("DB_PASSWORD", "boilerplate");
    try {
      DriverManager.getConnection(url, user, password).close();
    } catch (Exception ex) {
      assumeTrue(false, "Postgres required for context test. Run: ./scripts/manage.sh db-init");
    }
  }

  @Test
  void contextLoads() {}
}
