package com.loctran.Booking;

import com.loctran.Car.*;
import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import com.loctran.User.UpdateUserRequest;
import com.loctran.User.User;
import com.loctran.User.UserDAO;
import com.loctran.User.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServicesTest {

    @Mock
    private BookingDAO bookingDAO;
    @Mock
    private CarServices carServices;
    @Mock
    private UserService userService;
    private BookingServices underTest;

    @BeforeEach
    void setUp() {
        underTest = new BookingServices(bookingDAO, carServices, userService);
    }

    @Test
    void book() {
        Car car = new Car("7777", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(UUID.randomUUID(), "Loc");
        List<Car> availableCars = List.of(car);

        when(carServices.getAllCars()).thenReturn(availableCars);
        when(bookingDAO.AvailableCars(availableCars)).thenReturn(availableCars);
        when(carServices.getCar("7777")).thenReturn(car);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        underTest.Book(user, "7777");

        verify(bookingDAO).Booking(bookingCaptor.capture());

        Booking captured = bookingCaptor.getValue();

        assertThat(captured.getUsers()).isEqualTo(user);
        assertThat(captured.getCars()).isEqualTo(car);


    }

    @Test
    void willThrowAnExceptionWhenRegNumberAreNotRepresentOnBook() {
        String regNumber = "NOT_EXIST";
        Car car = new Car("7777", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(UUID.randomUUID(), "Loc");
        List<Car> availableCars = List.of(car);

        when(carServices.getAllCars()).thenReturn(availableCars);
        when(bookingDAO.AvailableCars(availableCars)).thenReturn(availableCars);

        assertThatThrownBy(() -> underTest.Book(user, regNumber)).isInstanceOf(IllegalStateException.class)
                .hasMessage("Invalid regNumber : " + regNumber);

       verify(bookingDAO, never()).Booking(any());


    }

    @Test
    void viewAllUserBooking() {
        UUID userId = UUID.randomUUID();
        underTest.ViewAllUserBooking(userId);

        verify(bookingDAO).ViewAllUserBooking(userId);
    }

    @Test
    void getAvailableElectricCars() {
        Car car1 = new Car("5555", new BigDecimal("12.34"), Brand.TESLA, true);
        Car car2 = new Car("6666", new BigDecimal("22.34"), Brand.MERCEDES, false);

        List<Car> getAllCars = List.of(car1, car2);
        List<Car> availableElectricCars = List.of(car1);

        when(carServices.getAllCars()).thenReturn(getAllCars);
        when(bookingDAO.AvailableCars(getAllCars)).thenReturn(availableElectricCars);

        List<Car> actual = underTest.getAvailableCars();

        assertThat(actual).isEqualTo(availableElectricCars);
        verify(carServices).getAllCars();
        verify(bookingDAO).AvailableCars(getAllCars);
    }

    @Test
    void canGetAvailableCars() {
        Car car1 = new Car("3333", new BigDecimal("12.34"), Brand.TESLA, true);
        Car car2 = new Car("4444", new BigDecimal("22.34"), Brand.MERCEDES, false);

        List<Car> getAllCars = List.of(car1, car2);
        List<Car> availableCars = List.of(car2);

        when(carServices.getAllCars()).thenReturn(getAllCars);
        when(bookingDAO.AvailableCars(getAllCars)).thenReturn(availableCars);

        List<Car> actual = underTest.getAvailableCars();

        assertThat(actual).isEqualTo(availableCars);
        verify(carServices).getAllCars();
        verify(bookingDAO).AvailableCars(getAllCars);
    }

    @Test
    void willThrowWhenGetAvailableCarsReturnEmptyList() {
        Car car1 = new Car("1111", new BigDecimal("12.34"), Brand.TESLA, true);
        Car car2 = new Car("2222", new BigDecimal("22.34"), Brand.MERCEDES, false);

        List<Car> getAllCars = List.of(car1, car2);

        when(carServices.getAllCars()).thenReturn(getAllCars);
        when(bookingDAO.AvailableCars(getAllCars)).thenReturn(List.of());

        assertThatThrownBy(() -> underTest.getAvailableCars())
                .isInstanceOf(ResourceNotFound.class).hasMessage("No cars available");
        verify(carServices).getAllCars();
        verify(bookingDAO).AvailableCars(getAllCars);
    }

    @Test
    void viewAllBooking() {
        underTest.viewAllBooking();

        verify(bookingDAO).ViewBooking();
    }

    @Test
    void deleteBooking() {
        UUID bookingId = UUID.randomUUID();

        Car car = new Car("1111", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(UUID.randomUUID(), "Loc");

        Booking booking = new Booking(bookingId, car, user);

        when(bookingDAO.findBookingById(bookingId)).thenReturn(Optional.of(booking));

        underTest.deleteBooking(bookingId);

        verify(bookingDAO).findBookingById(bookingId);
        verify(bookingDAO).deleteBooking(bookingId);
    }

    @Test
    void willThrowAnExceptionWhenNoUserBookingBeforeDeleteBooking() {
        UUID bookingId = UUID.randomUUID();

        when(bookingDAO.findBookingById(bookingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.deleteBooking(bookingId))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("No Booking with id: " + bookingId);

    }

    @Test
    void findBookingById(){
        UUID bookingId = UUID.randomUUID();
        Booking booking = mock(Booking.class);

        when(bookingDAO.findBookingById(bookingId)).thenReturn(Optional.of(booking));

        underTest.findBookingById(bookingId);

        verify(bookingDAO).findBookingById(bookingId);
    }

    @Test
    void willThrowAnExceptionWhenFindBookingByIdNotFound(){
        UUID bookingId = UUID.randomUUID();

        when(bookingDAO.findBookingById(bookingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findBookingById(bookingId))
                .isInstanceOf(ResourceNotFound.class)
                        .hasMessage("No Booking with id: " + bookingId);

        verify(bookingDAO).findBookingById(bookingId);
    }

    @Test
    void updateBooking() {
        UUID bookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String regNumber = "1111";

        Car car = new Car(regNumber, new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(userId, "Loc");

        Booking booking = new Booking(bookingId, car, user);

        User newUser = new User(userId, "Hoa");
        Car newCar = new Car(regNumber, new BigDecimal("32.34"), Brand.MERCEDES, false);

        when(bookingDAO.findBookingById(bookingId)).thenReturn(Optional.of(booking));
        when(userService.getUsersByID(userId)).thenReturn(newUser);
        when(carServices.getCar(regNumber)).thenReturn(newCar);


        UpdateBookingRequest updateBookingRequest = new UpdateBookingRequest(bookingId, newUser, newCar);
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(newUser.getName());
        UpdateCarRequest updateCarRequest = new UpdateCarRequest
                (newCar.getRegNumber(), newCar.getRentalPricePerDay(), newCar.getBrand(), newCar.isElectric());

        underTest.updateBooking(bookingId, updateBookingRequest, updateCarRequest, updateUserRequest);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        verify(bookingDAO).updateBooking(bookingCaptor.capture());
        verify(userService).updateUser(userId, updateUserRequest);
        verify(carServices).updateCar(regNumber, updateCarRequest);

        Booking bookingCaptorValue = bookingCaptor.getValue();

        assertThat(bookingCaptorValue.getId()).isEqualTo(bookingId);
        assertThat(bookingCaptorValue.getUsers()).isEqualTo(newUser);
        assertThat(bookingCaptorValue.getCars()).isEqualTo(newCar);

    }

    @Test
    void updateUserOnlyInBooking() {
        UUID bookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String regNumber = "1111";

        Car car = new Car(regNumber, new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(userId, "Loc");

        Booking booking = new Booking(bookingId, car, user);

        User newUser = new User(userId, "Hoa");

        when(bookingDAO.findBookingById(bookingId)).thenReturn(Optional.of(booking));
        when(userService.getUsersByID(userId)).thenReturn(newUser);


        UpdateBookingRequest updateBookingRequest = new UpdateBookingRequest(bookingId, newUser, null);
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(newUser.getName());

        underTest.updateBooking(bookingId, updateBookingRequest, null, updateUserRequest);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        verify(bookingDAO).updateBooking(bookingCaptor.capture());
        verify(userService).updateUser(userId, updateUserRequest);

        Booking bookingCaptorValue = bookingCaptor.getValue();

        assertThat(bookingCaptorValue.getId()).isEqualTo(bookingId);
        assertThat(bookingCaptorValue.getUsers()).isEqualTo(newUser);
        assertThat(bookingCaptorValue.getCars()).isEqualTo(car);
    }

    @Test
    void updateCarOnlyInBooking() {
        UUID bookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String regNumber = "1111";

        Car car = new Car(regNumber, new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(userId, "Loc");

        Booking booking = new Booking(bookingId, car, user);

        Car newCar = new Car(regNumber, new BigDecimal("32.34"), Brand.MERCEDES, false);

        when(bookingDAO.findBookingById(bookingId)).thenReturn(Optional.of(booking));
        when(carServices.getCar(regNumber)).thenReturn(newCar);


        UpdateBookingRequest updateBookingRequest = new UpdateBookingRequest(bookingId, null, newCar);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest
                (newCar.getRegNumber(), newCar.getRentalPricePerDay(), newCar.getBrand(), newCar.isElectric());

        underTest.updateBooking(bookingId, updateBookingRequest, updateCarRequest, null);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        verify(bookingDAO).updateBooking(bookingCaptor.capture());
        verify(carServices).updateCar(regNumber, updateCarRequest);

        Booking bookingCaptorValue = bookingCaptor.getValue();

        assertThat(bookingCaptorValue.getId()).isEqualTo(bookingId);
        assertThat(bookingCaptorValue.getUsers()).isEqualTo(user);
        assertThat(bookingCaptorValue.getCars()).isEqualTo(newCar);
    }

    @Test
    void willThrowAndExceptionWhenTryingToUpdateBookingId(){
        UUID bookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String regNumber = "1111";

        Car car = new Car(regNumber, new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(userId, "Loc");

        Booking booking = new Booking(bookingId, car, user);

        when(bookingDAO.findBookingById(bookingId)).thenReturn(Optional.of(booking));

        UpdateBookingRequest updateBookingRequest = new UpdateBookingRequest(UUID.randomUUID(), null, null);

        assertThatThrownBy(
                () -> underTest.updateBooking(bookingId, updateBookingRequest, null, null))
                .isInstanceOf(RequestValidationException.class).hasMessage("Id can't be changed");

        verify(bookingDAO, never()).updateBooking(any());

    }
    @Test
    void willThrowAnExceptionWhenNothingChangesInUpdateBooking(){
        UUID bookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String regNumber = "1111";

        Car car = new Car(regNumber, new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(userId, "Loc");

        Booking booking = new Booking(bookingId, car, user);

        when(bookingDAO.findBookingById(bookingId)).thenReturn(Optional.of(booking));

        UpdateBookingRequest updateBookingRequest = new UpdateBookingRequest(bookingId, null, null);

        assertThatThrownBy(() -> underTest.updateBooking(bookingId, updateBookingRequest, null, null))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");

        verify(bookingDAO, never()).updateBooking(any());
    }
}
