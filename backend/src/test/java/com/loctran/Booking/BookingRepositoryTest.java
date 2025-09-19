package com.loctran.Booking;

import com.loctran.Car.Brand;
import com.loctran.Car.Car;
import com.loctran.Car.CarRepository;
import com.loctran.User.User;
import com.loctran.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryTest {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CarRepository carRepository;


    @BeforeEach
    void setUp() {
    }

    @Test
    void findAvailableCarsFromList() {
        Car car1 = new Car("0193", new BigDecimal("12.34"), Brand.TESLA, true);
        Car car2 = new Car("2105", new BigDecimal("32.34"), Brand.MERCEDES, false);
        User user = new User("Loc", "password");

        car1 = carRepository.save(car1);
        car2 = carRepository.save(car2);
        user = userRepository.save(user);

        Booking booking = new Booking(
                car1, user
        );

        bookingRepository.save(booking);

        List<Car> cars = carRepository.findAll();
        List<Car> actual = bookingRepository.findAvailableCarsFromList(cars);

        assertThat(actual).isNotEmpty().contains(car2).doesNotContain(car1);
    }

    @Test
    void findBookingsByUserId() {
        Car car1 = new Car("1111", new BigDecimal("12.34"), Brand.TESLA, true);
        Car car2 = new Car("2222", new BigDecimal("32.34"), Brand.MERCEDES, false);
        User user = new User("Amanda", "password");

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

        assertThat(actual).isNotEmpty().contains(booking1, booking2);
    }
}