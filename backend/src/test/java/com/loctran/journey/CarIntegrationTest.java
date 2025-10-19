package com.loctran.journey;

import com.github.javafaker.Faker;
import com.loctran.car.Brand;
import com.loctran.car.Car;
import com.loctran.car.UpdateCarRequest;
import com.loctran.user.UserRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.shaded.com.google.common.io.Files;
import reactor.core.publisher.Mono;

import java.io.IOException;
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
        assertThat(cars).contains(request);

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


    @Test
    void canUploadAndDownloadCarImages() throws IOException {
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
        assertThat(cars).contains(request);

        Resource image = new ClassPathResource(
                "%s.png".formatted(request.getBrand().toString().toLowerCase())
        );

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", image);

        webTestClient.post().uri("/api/v1/cars/{regNumber}/car-images", regNumber)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange().expectStatus().isOk();

        String imageId = Objects.requireNonNull(webTestClient.get().uri("/api/v1/cars/{regNumber}", regNumber)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Car>() {
                })
                .returnResult().getResponseBody()).getCarImageId();

        assertThat(imageId).isNotNull();

        // download car image
        byte[] carImageDownloaded = webTestClient.get().uri("/api/v1/cars/{regNumber}/car-images", regNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(byte[].class)
                .returnResult().getResponseBody();

        byte[] actual = Files.toByteArray(image.getFile());

        assertThat(actual).isEqualTo(carImageDownloaded);
    }




}
