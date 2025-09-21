package com.loctran.journey;

import com.github.javafaker.Faker;
import com.loctran.Booking.BookingRequest;
import com.loctran.Car.Brand;
import com.loctran.Car.Car;
import com.loctran.Car.UpdateCarRequest;
import com.loctran.User.UserRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    protected String jwtToken;
    private static final Faker faker = new Faker();


    @BeforeEach
    void initJwtToken() {
        String username = faker.name().username() + "-" + System.currentTimeMillis();
        UserRegistrationRequest request = new UserRegistrationRequest(username, "password");

        jwtToken = Objects.requireNonNull(
                        webTestClient.post()
                                .uri("/api/v1/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(request), UserRegistrationRequest.class)
                                .exchange()
                                .expectStatus().isOk()
                                .returnResult(Void.class)
                                .getResponseHeaders()
                                .get(HttpHeaders.AUTHORIZATION))
                .get(0);
    }

    @Test
    void canRegisterACar(){
        String regNumber = UUID.randomUUID().toString().substring(0, 8);
        Car request = new Car(regNumber, new BigDecimal("322.10"), Brand.TESLA, true);

        webTestClient.post().uri("/api/v1/cars")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), Car.class)
                .exchange().expectStatus().isOk();

        //Get AllCars

        List<Car> cars = webTestClient.get().uri("/api/v1/cars")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Car>() {})
                .returnResult().getResponseBody();

        assertThat(cars).isNotNull();

        //Get Cars by id from api

        assertThat(cars).contains(request);

        webTestClient.get().uri("/api/v1/cars/{regNumber}", regNumber)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk().expectBody(new ParameterizedTypeReference<Car>() {})
                .isEqualTo(request);
    }

    @Test
    void canDeleteCar(){
        String regNumber = UUID.randomUUID().toString().substring(0, 8);
        Car request = new Car(regNumber, new BigDecimal("322.10"), Brand.TESLA, true);

        webTestClient.post().uri("/api/v1/cars")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), Car.class)
                .exchange().expectStatus().isOk();
        //Get AllCars

        List<Car> cars = webTestClient.get().uri("/api/v1/cars")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Car>() {})
                .returnResult().getResponseBody();

        assertThat(cars).isNotNull();

        //Get Cars by id from api

        webTestClient.delete().uri("/api/v1/cars/{regNumber}", regNumber)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk();

        webTestClient.get().uri("/api/v1/cars/{regNumber}", regNumber)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void canUpdateCar2(){
        String regNumber = UUID.randomUUID().toString().substring(0, 8);
        Car request = new Car(regNumber, new BigDecimal("322.10"), Brand.TESLA, true);

        webTestClient.post().uri("/api/v1/cars")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), Car.class)
                .exchange().expectStatus().isOk();

        //Get AllCars

        List<Car> cars = webTestClient.get().uri("/api/v1/cars")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Car>() {})
                .returnResult().getResponseBody();

        assertThat(cars).isNotNull();

        //Get Cars by id from api
        Car updateCar = new Car(regNumber, new BigDecimal("100.00"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                updateCar.getRegNumber(), updateCar.getRentalPricePerDay(), updateCar.getBrand(), updateCar.isElectric()
        );

        webTestClient.put().uri("/api/v1/cars/{regNumber}", regNumber).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .body(Mono.just(updateCarRequest), UpdateCarRequest.class)
                .exchange().expectStatus().isOk();

        Car actual = webTestClient.get().uri("/api/v1/cars/{regNumber}", regNumber)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Car>() {
                })
                .returnResult().getResponseBody();

        assertThat(actual).isEqualTo(updateCar);
    }

}
