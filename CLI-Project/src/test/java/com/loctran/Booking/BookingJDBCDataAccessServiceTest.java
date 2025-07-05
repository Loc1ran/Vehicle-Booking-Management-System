package com.loctran.Booking;

import com.loctran.AbstractDaoUnitTest;
import com.loctran.Car.Brand;
import com.loctran.Car.Car;
import com.loctran.Car.CarJDBCDataAccessService;
import com.loctran.Car.CarRowMapper;
import com.loctran.User.User;
import com.loctran.User.UserJDBCDataAccessService;
import com.loctran.User.UserRowMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class BookingJDBCDataAccessServiceTest extends AbstractDaoUnitTest {

    private BookingJDBCDataAccessService underTest;
    private CarJDBCDataAccessService carJDBCDataAccessService;
    private UserJDBCDataAccessService userJDBCDataAccessService;
    private final BookingRowMapper bookingRowMapper = new BookingRowMapper();
    private final CarRowMapper carRowMapper = new CarRowMapper();
    private final UserRowMapper userRowMapper = new UserRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new BookingJDBCDataAccessService(
                getJdbcTemplate(),
                bookingRowMapper, carRowMapper
        );

        carJDBCDataAccessService = new CarJDBCDataAccessService(
                getJdbcTemplate(), carRowMapper
        );

        userJDBCDataAccessService = new UserJDBCDataAccessService(
                getJdbcTemplate(), userRowMapper
        );

    }

    @AfterEach
    void cleanDatabase() {
        getJdbcTemplate().execute("TRUNCATE booking CASCADE");
        getJdbcTemplate().execute("TRUNCATE car CASCADE");
        getJdbcTemplate().execute("TRUNCATE user_info CASCADE");
    }



    @Test
    void viewBooking() {
        //Given
        Car car = new Car("1234", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(UUID.randomUUID(), "Loc");

        carJDBCDataAccessService.saveCar(car);
        userJDBCDataAccessService.saveUser(user);

        Booking booking = new Booking(
                UUID.randomUUID(), car, user
        );
        underTest.Booking(booking);
        //When
        List<Booking> bookings = underTest.ViewBooking();

        //Then
        assertThat(bookings).isNotEmpty();

    }

    @Test
    void booking() {
        Car car = new Car("2213", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(UUID.randomUUID(), "Ava");

        carJDBCDataAccessService.saveCar(car);
        userJDBCDataAccessService.saveUser(user);

        Booking booking = new Booking(
                UUID.randomUUID(), car, user
        );

        underTest.Booking(booking);

        List<Booking> viewBookings = underTest.ViewBooking();

        assertThat(viewBookings).isNotEmpty();
    }

    @Test
    void availableCars() {
        Car car1 = new Car("0193", new BigDecimal("12.34"), Brand.TESLA, true);
        Car car2 = new Car("2105", new BigDecimal("32.34"), Brand.MERCEDES, false);
        User user = new User(UUID.randomUUID(), "Loc");

        carJDBCDataAccessService.saveCar(car1);
        carJDBCDataAccessService.saveCar(car2);
        userJDBCDataAccessService.saveUser(user);

         Booking booking = new Booking(
                 UUID.randomUUID(), car1, user
         );

         underTest.Booking(booking);

         List<Car> cars = carJDBCDataAccessService.getCars();
         List<Car> actual = underTest.AvailableCars(cars);

         assertThat(actual).isNotEmpty().containsExactly(car2);

    }

    @Test
    void deleteBooking() {
        Car car = new Car("7777", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(UUID.randomUUID(), "Ana");

        carJDBCDataAccessService.saveCar(car);
        userJDBCDataAccessService.saveUser(user);

        UUID bookingId = UUID.randomUUID();

        Booking booking = new Booking(
                bookingId, car, user
        );

        underTest.Booking(booking);

        underTest.deleteBooking(bookingId);

        Optional<Booking> viewBookings = underTest.findBookingById(bookingId);

        assertThat(viewBookings).isNotPresent();
    }

    @Test
    void viewAllUserBooking() {
        UUID userId = UUID.randomUUID();
        Car car1 = new Car("1111", new BigDecimal("12.34"), Brand.TESLA, true);
        Car car2 = new Car("2222", new BigDecimal("32.34"), Brand.MERCEDES, false);
        User user = new User(userId, "Amanda");

        carJDBCDataAccessService.saveCar(car1);
        carJDBCDataAccessService.saveCar(car2);
        userJDBCDataAccessService.saveUser(user);

        Booking booking1 = new Booking(
                UUID.randomUUID(), car1, user
        );

        Booking booking2 = new Booking(UUID.randomUUID(), car2, user);

        underTest.Booking(booking1);
        underTest.Booking(booking2);

        List<Booking> actual = underTest.ViewAllUserBooking(userId);

        assertThat(actual).isNotEmpty().containsExactly(booking1, booking2);

   }

    @Test
    void updateBooking() {
        UUID bookingId = UUID.randomUUID();
        UUID userID = UUID.randomUUID();

        Car car = new Car("6666", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(userID, "Ana");

        carJDBCDataAccessService.saveCar(car);
        userJDBCDataAccessService.saveUser(user);


        Booking booking = new Booking(
                bookingId, car, user
        );

        underTest.Booking(booking);

        Brand newBrand = Brand.MERCEDES;
        BigDecimal newRentalPrice = new BigDecimal("38.21");
        String newName = "Loc";


        Car carUpdate = new Car();
        carUpdate.setRegNumber("6666");
        carUpdate.setBrand(newBrand);
        carUpdate.setRentalPricePerDay(newRentalPrice);
        carUpdate.setElectric(false);

        User userUpdate = new User();
        userUpdate.setId(userID);
        userUpdate.setName("Loc");
        List<Booking> old = underTest.ViewBooking();

        Booking bookingUpdate = new Booking();
        bookingUpdate.setId(bookingId);
        bookingUpdate.setCars(carUpdate);
        bookingUpdate.setUsers(userUpdate);

        underTest.updateBooking(bookingUpdate);
        Optional<Booking> actual = underTest.findBookingById(bookingId);
        assertThat(actual).isPresent().hasValueSatisfying(b -> {
            assertThat(b.getCars()).isEqualTo(carUpdate);
            assertThat(b.getUsers()).isEqualTo(userUpdate);
        });


    }

    @Test
    void findBookingById() {
        Car car = new Car("2341", new BigDecimal("12.34"), Brand.TESLA, true);
        User user = new User(UUID.randomUUID(), "Jas");

        carJDBCDataAccessService.saveCar(car);
        userJDBCDataAccessService.saveUser(user);

        UUID bookingId = UUID.randomUUID();
        Booking booking = new Booking(
                bookingId, car, user
        );
        underTest.Booking(booking);

        Optional<Booking> actual = underTest.findBookingById(bookingId);
        assertThat(actual).isPresent().hasValueSatisfying(b -> {
            assertThat(b.getId()).isEqualTo(bookingId);
            assertThat(b.getCars()).isEqualTo(car);
            assertThat(b.getUsers()).isEqualTo(user);
        });
    }

    @Test
    void willReturnEmptyWhenSelectBookingById(){
        UUID NEVER_USED_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

        var actual = underTest.findBookingById(NEVER_USED_UUID);

        assertThat(actual).isEmpty();
    }
}