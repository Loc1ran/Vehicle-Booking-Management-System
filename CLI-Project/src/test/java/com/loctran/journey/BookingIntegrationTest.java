package com.loctran.journey;

import com.loctran.Booking.Booking;
import com.loctran.Booking.BookingRequest;
import com.loctran.Booking.BookingUpdateWrapper;
import com.loctran.Car.Brand;
import com.loctran.Car.Car;
import com.loctran.Car.CarServices;
import com.loctran.User.UpdateUserRequest;
import com.loctran.User.User;
import com.loctran.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Test
    void canRegisterABooking() {
        UUID userID = UUID.randomUUID();
        String regNumber = UUID.randomUUID().toString().substring(0, 8);
        //create registration request
        Car car = new Car(regNumber, new BigDecimal("322.10"), Brand.TESLA, true);
        User user = new User(userID, "Hoa");

        carServices.saveCar(car);
        userService.saveUser(user);

        BookingRequest request = new BookingRequest(user, regNumber);
        // send a post request
        webTestClient.post().uri("/api/v1/booking").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), BookingRequest.class)
                .exchange().expectStatus().isOk();
        // get all booking
        List<Booking> allBooking = webTestClient.get().uri("/api/v1/booking")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Booking>() {
                })
                .returnResult().getResponseBody();

        assertThat(allBooking).isNotNull();

        //get booking by id from api
        Booking booking = allBooking.stream().filter(b -> b.getCars().getRegNumber().equals(regNumber)).findFirst().orElseThrow();
        UUID bookingId = booking.getId();

        Booking expected = new Booking(car, user);
        expected.setId(bookingId);

        assertThat(allBooking).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(
                expected
        );

        webTestClient.get().uri("/api/v1/booking/{id}", bookingId)
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Booking>() {
                })
                .isEqualTo(booking);

    }

    @Test
    void canDeleteBooking() {
        UUID userID = UUID.randomUUID();
        String regNumber = UUID.randomUUID().toString().substring(0, 8);
        //create registration request
        Car car = new Car(regNumber, new BigDecimal("322.10"), Brand.TESLA, true);
        User user = new User(userID, "Hoa");

        carServices.saveCar(car);
        userService.saveUser(user);

        BookingRequest request = new BookingRequest(user, regNumber);
        // send a post request
        webTestClient.post().uri("/api/v1/booking").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), BookingRequest.class)
                .exchange().expectStatus().isOk();
        // get all booking
        List<Booking> allBooking = webTestClient.get().uri("/api/v1/booking")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Booking>() {
                })
                .returnResult().getResponseBody();

        assertThat(allBooking).isNotNull();

        //get booking by id from api
        Booking booking = allBooking.stream().filter(b -> b.getCars().getRegNumber().equals(regNumber)).findFirst().orElseThrow();
        UUID bookingId = booking.getId();

        webTestClient.delete().uri("/api/v1/booking/{id}", bookingId).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();


        webTestClient.get().uri("/api/v1/booking/{id}", bookingId)
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isNotFound();
    }

    @Test
    void canUpdateBooking() {
        UUID userID = UUID.randomUUID();
        String regNumber = UUID.randomUUID().toString().substring(0, 8);
        //create registration request
        Car car = new Car(regNumber, new BigDecimal("322.10"), Brand.TESLA, true);
        User user = new User(userID, "Hoa");

        carServices.saveCar(car);
        userService.saveUser(user);

        BookingRequest request = new BookingRequest(user, regNumber);
        // send a post request
        webTestClient.post().uri("/api/v1/booking").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(request), BookingRequest.class)
                .exchange().expectStatus().isOk();
        // get all booking
        List<Booking> allBooking = webTestClient.get().uri("/api/v1/booking")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Booking>() {
                })
                .returnResult().getResponseBody();

        assertThat(allBooking).isNotNull();

        //get booking by id from api
        Booking booking = allBooking.stream().filter(b -> b.getCars().getRegNumber().equals(regNumber)).findFirst().orElseThrow();
        UUID bookingId = booking.getId();

        String newName = "Loc";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(newName);
        BookingUpdateWrapper bookingUpdateWrapper = new BookingUpdateWrapper(
                null, null, updateUserRequest
        );

        webTestClient.put().uri("/api/v1/booking/{id}", bookingId).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookingUpdateWrapper), BookingUpdateWrapper.class)
                .exchange().expectStatus().isOk();


        Booking updateBooking = webTestClient.get().uri("/api/v1/booking/{id}", bookingId)
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectBody(Booking.class).returnResult().getResponseBody();

        Booking expected = new Booking(car, new User(userID, newName));
        expected.setId(bookingId);

        assertThat(updateBooking).isEqualTo(expected);
    }
}
