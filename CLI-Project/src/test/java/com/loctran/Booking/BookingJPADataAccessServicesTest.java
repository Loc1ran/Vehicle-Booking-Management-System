package com.loctran.Booking;

import com.loctran.AbstractDaoUnitTest;
import com.loctran.Car.Brand;
import com.loctran.Car.Car;
import com.loctran.Car.CarRepository;
import com.loctran.TestContainersTest;
import com.loctran.User.User;
import com.loctran.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingJPADataAccessServicesTest extends AbstractDaoUnitTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void viewBooking() {
        Car car = new Car("1234", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User("Loc");

        car = carRepository.save(car);
        user = userRepository.save(user);

        Booking booking = new Booking(
                car, user
        );
        bookingRepository.save(booking);
        //When
        List<Booking> bookings = bookingRepository.findAll();

        //Then
        assertThat(bookings).isNotEmpty();
    }

    @Test
    void booking() {
        Car car = new Car("2213", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User("Ava");

        car = carRepository.save(car);
        user = userRepository.save(user);

        Booking booking = new Booking(
                car, user
        );

        bookingRepository.save(booking);

        List<Booking> viewBookings = bookingRepository.findAll();

        assertThat(viewBookings).isNotEmpty();
    }

    @Test
    void availableCars() {
        Car car1 = new Car("0193", new BigDecimal("12.34"), Brand.TESLA, true);
        Car car2 = new Car("2105", new BigDecimal("32.34"), Brand.MERCEDES, false);
        User user = new User("Loc");

        car1 = carRepository.save(car1);
        car2 = carRepository.save(car2);
        user = userRepository.save(user);

        Booking booking = new Booking(
                car1, user
        );

        bookingRepository.save(booking);

        List<Car> cars = carRepository.findAll();
        List<Car> actual = bookingRepository.findAvailableCarsFromList(cars);

        assertThat(actual).isNotEmpty().containsExactly(car2);
    }

    @Test
    void deleteBooking() {
        Car car = new Car("7777", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User("Ana");

        car = carRepository.save(car);
        user = userRepository.save(user);

        Booking booking = new Booking(
                car, user
        );

        booking = bookingRepository.save(booking);

        bookingRepository.deleteById(booking.getId());

        Optional<Booking> viewBookings = bookingRepository.findById(booking.getId());

        assertThat(viewBookings).isNotPresent();
    }

    @Test
    void viewAllUserBooking() {
        Car car1 = new Car("1111", new BigDecimal("12.34"), Brand.TESLA, true);
        Car car2 = new Car("2222", new BigDecimal("32.34"), Brand.MERCEDES, false);
        User user = new User("Amanda");

        car1 = carRepository.save(car1);
        car2 = carRepository.save(car2);
        user = userRepository.save(user);

        Booking booking1 = new Booking(
                car1, user
        );

        Booking booking2 = new Booking(car2, user);

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<Booking> actual =  bookingRepository.findBookingsByUserId(user.getId());

        assertThat(actual).isNotEmpty().containsExactly(booking1, booking2);
    }

    @Test
    void updateBooking() {
        Car car = new Car("6666", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User("Ana");

        car = carRepository.save(car);
        user = userRepository.save(user);


        Booking booking = new Booking(
                car, user
        );

        booking = bookingRepository.save(booking);

        Brand newBrand = Brand.MERCEDES;
        BigDecimal newRentalPrice = new BigDecimal("38.21");
        String newName = "Loc";


        Car carUpdate = new Car();
        carUpdate.setRegNumber("6666");
        carUpdate.setBrand(newBrand);
        carUpdate.setRentalPricePerDay(newRentalPrice);
        carUpdate.setElectric(false);

        carUpdate = carRepository.save(carUpdate);

        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setName("Loc");

        userUpdate = userRepository.save(userUpdate);


        Booking bookingUpdate = bookingRepository.findById(booking.getId()).orElseThrow();
        bookingUpdate.setCars(carUpdate);
        bookingUpdate.setUsers(userUpdate);


        bookingRepository.save(bookingUpdate);
        Optional<Booking> actual = bookingRepository.findById(booking.getId());

        Car finalCarUpdate = carUpdate;
        User finalUserUpdate = userUpdate;
        
        assertThat(actual).isPresent().hasValueSatisfying(b -> {
            assertThat(b.getCars()).isEqualTo(finalCarUpdate);
            assertThat(b.getUsers()).isEqualTo(finalUserUpdate);
        });


    }

    @Test
    void findBookingById() {
        Car car = new Car("2341", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User("Jas");

        car = carRepository.save(car);
        user = userRepository.save(user);

        Booking booking = new Booking(
                car, user
        );

        booking = bookingRepository.save(booking);

        Optional<Booking> actual = bookingRepository.findById(booking.getId());

        Car finalCar = car;
        User finalUser = user;
        Booking finalBooking = booking;

        assertThat(actual).isPresent().hasValueSatisfying(b -> {
            assertThat(b.getId()).isEqualTo(finalBooking.getId());
            assertThat(b.getCars()).isEqualTo(finalCar);
            assertThat(b.getUsers()).isEqualTo(finalUser);
        });
    }

    @Test
    void willReturnEmptyWhenSelectBookingById(){
        UUID NEVER_USED_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

        var actual = bookingRepository.findById(NEVER_USED_UUID);

        assertThat(actual).isEmpty();
    }
}