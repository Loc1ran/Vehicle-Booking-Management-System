package com.loctran.journey;

import com.github.javafaker.Faker;
import com.loctran.User.UpdateUserRequest;
import com.loctran.User.User;
import com.loctran.User.UserDTO;
import com.loctran.User.UserRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {
    @Autowired
    WebTestClient webTestClient;
    private final Faker faker = new Faker();

    @Test
    void canRegisterAUser() {
        String name = faker.name().username() + "-" + System.currentTimeMillis();
        UserRegistrationRequest request = new UserRegistrationRequest(name, "password");

        String jwtToken = Objects.requireNonNull(webTestClient.post().uri("/api/v1/users").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(request), User.class)
                        .exchange().expectStatus().isOk().returnResult(Void.class)
                        .getResponseHeaders()
                        .get(HttpHeaders.AUTHORIZATION))
                .get(0);

        //get All users
        List<UserDTO> getAllUsers = webTestClient.get().uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<UserDTO>() {
                }).returnResult().getResponseBody();

        assertThat(getAllUsers).isNotNull();

        //get user by id from api
        UUID id = getAllUsers.stream().filter(u -> u.name().equals(name)).findFirst().orElseThrow().id();

        UserDTO expectedUser = new UserDTO(id, name, List.of("ROLE_USER"), name);

        assertThat(getAllUsers).contains(expectedUser);

        webTestClient.get().uri("/api/v1/users/{userId}", id).accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk().expectBody(new ParameterizedTypeReference<UserDTO>() {
                }).isEqualTo(expectedUser);

    }

    @Test
    void canDeleteUser() {
        String name = faker.name().username() + "-" + System.currentTimeMillis();
        UserRegistrationRequest request1 = new UserRegistrationRequest(name, "password");
        UserRegistrationRequest request2 = new UserRegistrationRequest(name + "us", "password");

        webTestClient.post().uri("/api/v1/users").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request1), User.class)
                .exchange().expectStatus().isOk();

        String jwtToken = Objects.requireNonNull(webTestClient.post().uri("/api/v1/users").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request2), User.class)
                        .exchange().expectStatus().isOk().returnResult(Void.class)
                        .getResponseHeaders()
                        .get(HttpHeaders.AUTHORIZATION))
                .get(0);

        //get All users
        List<UserDTO> getAllUsers = webTestClient.get().uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<UserDTO>() {
                }).returnResult().getResponseBody();

        assertThat(getAllUsers).isNotNull();

        UUID id = getAllUsers.stream().filter(u -> u.name().equals(name)).findFirst().orElseThrow().id();

        //get user by id from api

        webTestClient.delete().uri("/api/v1/users/{userId}", id).accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk();

        webTestClient.get().uri("/api/v1/users/{userId}", id).accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isNotFound();

    }

    @Test
    void canUpdateUser() {
        String name = faker.name().username() + "-" + System.currentTimeMillis();
        UserRegistrationRequest request = new UserRegistrationRequest(name, "password");
        UserRegistrationRequest request2 = new UserRegistrationRequest(name + "us", "password");

        webTestClient.post().uri("/api/v1/users").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), User.class)
                .exchange().expectStatus().isOk().returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        String jwtToken = webTestClient.post().uri("/api/v1/users").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request2), User.class)
                .exchange().expectStatus().isOk().returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        //get All users
        List<UserDTO> getAllUsers = webTestClient.get().uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<UserDTO>() {
                }).returnResult().getResponseBody();

        assertThat(getAllUsers).isNotNull();

        //get user by id from api
        UUID id = getAllUsers.stream().filter(u -> u.name().equals(name)).findFirst().orElseThrow().id();
        String newName = faker.name().username() + "-" + System.currentTimeMillis();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(newName);

        webTestClient.put().uri("/api/v1/users/{userId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUserRequest), UpdateUserRequest.class)
                .exchange().expectStatus().isOk();

        UserDTO actual = webTestClient.get().uri("/api/v1/users/{userId}", id).accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk().expectBody(new ParameterizedTypeReference<UserDTO>() {})
                .returnResult().getResponseBody();

        UserDTO expected = new UserDTO(id, newName, List.of("ROLE_USER"), newName);

        assertThat(actual).isEqualTo(expected);
    }
}