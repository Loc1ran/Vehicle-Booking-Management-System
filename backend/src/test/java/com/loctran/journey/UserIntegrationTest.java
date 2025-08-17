package com.loctran.journey;

import com.loctran.User.UpdateUserRequest;
import com.loctran.User.User;
import org.hibernate.cfg.Environment;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void canRegisterAUser() {
        UUID userId = UUID.randomUUID();
        User request = new User(userId,"Hoa");

        webTestClient.post().uri("/api/v1/users").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), User.class)
                .exchange().expectStatus().isOk();

        //get All users
        List<User> getAllUsers = webTestClient.get().uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {
                }).returnResult().getResponseBody();

        assertThat(getAllUsers).isNotNull();

        //get user by id from api

        assertThat(getAllUsers).contains(request);

        webTestClient.get().uri("/api/v1/users/{userId}", userId).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk().expectBody(new ParameterizedTypeReference<User>() {
                }).isEqualTo(request);

    }

    @Test
    void canDeleteUser() {
        UUID userId = UUID.randomUUID();
        User request = new User(userId,"Hoa");

        webTestClient.post().uri("/api/v1/users").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), User.class)
                .exchange().expectStatus().isOk();

        //get All users
        List<User> getAllUsers = webTestClient.get().uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {
                }).returnResult().getResponseBody();

        assertThat(getAllUsers).isNotNull();

        //get user by id from api

        webTestClient.delete().uri("/api/v1/users/{userId}", userId).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();

        webTestClient.get().uri("/api/v1/users/{userId}", userId).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isNotFound();

    }

    @Test
    void canUpdateUser() {
        UUID userId = UUID.randomUUID();
        User request = new User(userId,"Hoa");

        webTestClient.post().uri("/api/v1/users").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), User.class)
                .exchange().expectStatus().isOk();

        //get All users
        List<User> getAllUsers = webTestClient.get().uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {
                }).returnResult().getResponseBody();

        assertThat(getAllUsers).isNotNull();

        //get user by id from api

        String newName = "Loc";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(newName);

        webTestClient.put().uri("/api/v1/users/{userId}", userId).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUserRequest), UpdateUserRequest.class)
                .exchange().expectStatus().isOk();

        User actual = webTestClient.get().uri("/api/v1/users/{userId}", userId).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk().expectBody(new ParameterizedTypeReference<User>() {})
                .returnResult().getResponseBody();

        User expected = new User(userId, newName);

        assertThat(actual).isEqualTo(expected);

    }
}