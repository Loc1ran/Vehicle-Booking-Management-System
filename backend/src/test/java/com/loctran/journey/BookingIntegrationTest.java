package com.loctran.journey;

import com.github.javafaker.Faker;
import com.loctran.Booking.Booking;
import com.loctran.Booking.BookingDTO;
import com.loctran.Booking.BookingRequest;
import com.loctran.Booking.BookingUpdateWrapper;
import com.loctran.Car.Brand;
import com.loctran.Car.Car;
import com.loctran.Car.CarServices;
import com.loctran.Car.UpdateCarRequest;
import com.loctran.User.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private CarServices carServices;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDTOMapper userDTOMapper;
    private static final Faker faker = new Faker();

    @Test
    void canRegisterABooking() {
        String name = faker.name().username() + "-" + System.currentTimeMillis();
        String regNumber = UUID.randomUUID().toString().substring(0, 8);

        Car car = new Car(regNumber, new BigDecimal("322.10"), Brand.TESLA, true);
        carServices.saveCar(car);

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, "password");

        String jwtToken = Objects.requireNonNull(webTestClient.post()
                        .uri("/api/v1/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(userRequest), UserRegistrationRequest.class)
                        .exchange()
                        .expectStatus().isOk()
                        .returnResult(Void.class)
                        .getResponseHeaders()
                        .get(HttpHeaders.AUTHORIZATION))
                .get(0);

        User user = userService.findByName(name);

        BookingRequest request = new BookingRequest(user, regNumber);

        webTestClient.post()
                .uri("/api/v1/booking")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), BookingRequest.class)
                .exchange()
                .expectStatus().isOk();

        List<BookingDTO> allBookings = webTestClient.get()
                .uri("/api/v1/booking")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<BookingDTO>() {})
                .returnResult()
                .getResponseBody();

        assertThat(allBookings).isNotNull();

        BookingDTO booking = allBookings.stream()
                .filter(b -> b.car().getRegNumber().equals(regNumber))
                .findFirst()
                .orElseThrow();

        UUID bookingId = booking.id();
        BookingDTO expectedBooking = new BookingDTO(bookingId, car, userDTOMapper.apply(user));

        assertThat(allBookings)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedBooking);

        webTestClient.get()
                .uri("/api/v1/booking/{id}", bookingId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BookingDTO>() {})
                .isEqualTo(booking);

    }

    @Test
    void canDeleteBooking() {
        String name = faker.name().username() + "-" + System.currentTimeMillis();
        String regNumber = UUID.randomUUID().toString().substring(0, 8);

        Car car = new Car(regNumber, new BigDecimal("322.10"), Brand.TESLA, true);
        carServices.saveCar(car);

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, "password");

        String jwtToken = Objects.requireNonNull(webTestClient.post()
                        .uri("/api/v1/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(userRequest), UserRegistrationRequest.class)
                        .exchange()
                        .expectStatus().isOk()
                        .returnResult(Void.class)
                        .getResponseHeaders()
                        .get(HttpHeaders.AUTHORIZATION))
                .get(0);

        User user = userService.findByName(name);

        BookingRequest request = new BookingRequest(user, regNumber);
        // send a post request
        webTestClient.post().uri("/api/v1/booking")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), BookingRequest.class)
                .exchange().expectStatus().isOk();
        // get all booking
        List<BookingDTO> allBookings = webTestClient.get().uri("/api/v1/booking")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<BookingDTO>() {
                })
                .returnResult().getResponseBody();

        assertThat(allBookings).isNotNull();

        //get booking by id from api
        BookingDTO booking = allBookings.stream()
                .filter(b -> b.car().getRegNumber().equals(regNumber))
                .findFirst()
                .orElseThrow();

        UUID bookingId = booking.id();

        webTestClient.delete().uri("/api/v1/booking/{id}", bookingId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk();


        webTestClient.get().uri("/api/v1/booking/{id}", bookingId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void canUpdateBooking() {
        String name = faker.name().username() + "-" + System.currentTimeMillis();
        String regNumber = UUID.randomUUID().toString().substring(0, 8);

        Car car = new Car(regNumber, new BigDecimal("322.10"), Brand.TESLA, true);
        carServices.saveCar(car);

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, "password");

        String jwtToken = Objects.requireNonNull(webTestClient.post()
                        .uri("/api/v1/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(userRequest), UserRegistrationRequest.class)
                        .exchange()
                        .expectStatus().isOk()
                        .returnResult(Void.class)
                        .getResponseHeaders()
                        .get(HttpHeaders.AUTHORIZATION))
                .get(0);

        User user = userService.findByName(name);

        BookingRequest request = new BookingRequest(user, regNumber);
        // send a post request
        webTestClient.post().uri("/api/v1/booking")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), BookingRequest.class)
                .exchange().expectStatus().isOk();
        // get all booking
        List<BookingDTO> allBookings = webTestClient.get().uri("/api/v1/booking")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<BookingDTO>() {
                })
                .returnResult().getResponseBody();

        assertThat(allBookings).isNotNull();

        //get booking by id from api
        BookingDTO booking = allBookings.stream()
                .filter(b -> b.car().getRegNumber().equals(regNumber))
                .findFirst()
                .orElseThrow();

        UUID bookingId = booking.id();

        String newPrice = "123.21";
        UpdateCarRequest updateCarRequest = new UpdateCarRequest(regNumber, new BigDecimal(newPrice), null, true );
        BookingUpdateWrapper bookingUpdateWrapper = new BookingUpdateWrapper(
                null, updateCarRequest, null
        );

        webTestClient.put().uri("/api/v1/booking/{id}", bookingId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookingUpdateWrapper), BookingUpdateWrapper.class)
                .exchange().expectStatus().isOk();


        BookingDTO updatedBooking = webTestClient.get().uri("/api/v1/booking/{id}", bookingId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookingDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(updatedBooking).isNotNull();
        assertThat(updatedBooking.car().getRegNumber()).isEqualTo(regNumber);
        assertThat(updatedBooking.car().getRentalPricePerDay()).isEqualTo(newPrice);
        assertThat(updatedBooking.car().getBrand()).isEqualTo(Brand.TESLA);
        assertThat(updatedBooking.car().isElectric()).isEqualTo(true);
        assertThat(updatedBooking.user()).isEqualTo(userDTOMapper.apply(user));
    }
}
