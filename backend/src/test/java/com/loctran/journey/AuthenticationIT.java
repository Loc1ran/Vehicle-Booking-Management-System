package com.loctran.journey;


import com.github.javafaker.Faker;
import com.loctran.user.UserDTO;
import com.loctran.user.UserRegistrationRequest;
import com.loctran.auth.AuthenticationRequest;
import com.loctran.auth.AuthenticationResponse;
import com.loctran.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationIT {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    private JWTUtil jwtUtil;

    private static final Faker faker = new Faker();
    private static final String AUTH_PATH = "/api/v1/auth";
    private static final String USER_PATH = "/api/v1/users";


    @Test
    void canLoginUser() {
        String name = faker.name().username() + "-" + System.currentTimeMillis();
        UserRegistrationRequest request = new UserRegistrationRequest(name, "password");

        //get All users
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                name,"password"
        );

        webTestClient.post().uri(USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserRegistrationRequest.class)
                .exchange().expectStatus().isOk();



        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post().uri(AUTH_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

        String loginToken = Objects.requireNonNull(result.getResponseHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);

        AuthenticationResponse response = result.getResponseBody();

        assert response != null;
        UserDTO userDTO = response.userDTO();

        assertThat(jwtUtil.isTokenValid(loginToken, userDTO.username())).isTrue();

        assertThat(userDTO.name()).isEqualTo(name);
        assertThat(userDTO.username()).isEqualTo(name);
        assertThat(userDTO.roles()).isEqualTo(List.of("ROLE_USER"));
    }
}
