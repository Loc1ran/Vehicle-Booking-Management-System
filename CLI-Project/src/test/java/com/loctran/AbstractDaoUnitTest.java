package com.loctran;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;


@Testcontainers
public abstract class AbstractDaoUnitTest {

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()).load();
        flyway.migrate();
        System.out.println();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("dao-unit-test").withUsername("ltt4456").withPassword("password").withCommand("postgres", "-c", "max_connections=1000");

    @DynamicPropertySource
    private static void registerDateSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    private static DataSource getDataSource(){
        return DataSourceBuilder.create().driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword()).build();

    }

    protected static JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }

}
