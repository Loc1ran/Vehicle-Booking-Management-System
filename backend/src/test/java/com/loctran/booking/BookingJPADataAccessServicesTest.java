package com.loctran.Booking;

import com.loctran.Car.Brand;
import com.loctran.Car.Car;
import com.loctran.User.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class BookingJPADataAccessServicesTest {
    private BookingJPADataAccessServices underTest;
    private AutoCloseable autoCloseable;


    @Mock
    private BookingRepository bookingRepository;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new BookingJPADataAccessServices(bookingRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void viewBooking() {
        Page<Booking> page = mock(Page.class);
        List<Booking> bookings = Arrays.asList(new Booking());

        when(page.getContent()).thenReturn(bookings);
        when(bookingRepository.findAll(any(Pageable.class))).thenReturn(page);

        // When
        List<Booking> expected = underTest.ViewBooking();

        // Then
        assertThat(expected).isEqualTo(bookings);
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepository).findAll(captor.capture());
        assertThat(captor.getValue()).isEqualTo(Pageable.ofSize(100));
    }

    @Test
    void booking() {
        Car car = new Car("2213", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(UUID.randomUUID(), "Ava", "password");

        Booking booking = new Booking(
                UUID.randomUUID(), car, user
        );

        underTest.Booking(booking);

        verify(bookingRepository).save(booking);
    }

    @Test
    void availableCars() {
        List<Car> cars = Arrays.asList(new Car("1111", new BigDecimal("12.34"), Brand.TESLA, true),
                new Car("2222", new BigDecimal("12.34"), Brand.TESLA, true));

        underTest.AvailableCars(cars);

        verify(bookingRepository).findAvailableCarsFromList(cars);

    }

    @Test
    void deleteBooking() {
        UUID bookingId = UUID.randomUUID();

        underTest.deleteBooking(bookingId);

        verify(bookingRepository).deleteById(bookingId);

    }

    @Test
    void viewAllUserBooking() {
        UUID userID = UUID.randomUUID();

        underTest.ViewAllUserBooking(userID);

        verify(bookingRepository).findBookingsByUserId(userID);

    }

    @Test
    void updateBooking() {
        Car car = new Car("1234", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(UUID.randomUUID(), "Ava", "password");

        Booking booking = new Booking(
                UUID.randomUUID(), car, user
        );

        underTest.updateBooking(booking);
        verify(bookingRepository).save(booking);
    }

    @Test
    void findBookingById() {
        UUID bookingId = UUID.randomUUID();

        underTest.findBookingById(bookingId);

        verify(bookingRepository).findById(bookingId);
    }
}