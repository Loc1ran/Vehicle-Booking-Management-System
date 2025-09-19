package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.Car.CarRowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("bookingJDBC")
public class BookingJDBCDataAccessService implements BookingDAO{

    private final JdbcTemplate jdbcTemplate;
    private final BookingRowMapper bookingRowMapper;
    private final CarRowMapper carRowMapper;

    public BookingJDBCDataAccessService(JdbcTemplate jdbcTemplate, BookingRowMapper bookingRowMapper, CarRowMapper carRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookingRowMapper = bookingRowMapper;
        this.carRowMapper = carRowMapper;
    }

    @Override
    public List<Booking> ViewBooking() {
        var sql = """
        SELECT\s
            b.id AS booking_id,
        
            c.reg_number,
            c.rental_price_per_day,
            c.brand,
            c.is_electric,
        
            u.id AS user_id,
            u.name,
            u.password
        
        FROM booking b
        JOIN car c ON b.car_id = c.reg_number
        JOIN user_info u ON b.user_id = u.id
        """;

        return jdbcTemplate.query(sql, bookingRowMapper);
    }

    @Override
    public void Booking(Booking booking) {
        if ( booking.getId() == null){
            var sql = """
                INSERT INTO Booking (car_id, user_id)
                VALUES (?, ?)
                """;
            int result = jdbcTemplate.update(sql, booking.getCars().getRegNumber(), booking.getUsers().getId());
            System.out.println("jdbcTemplate update result :" + result);
        } else{
            var sql = """
                INSERT INTO Booking (id, car_id, user_id)
                VALUES (?, ?, ?)
                """;
            int result = jdbcTemplate.update(sql, booking.getId(), booking.getCars().getRegNumber(), booking.getUsers().getId());
            System.out.println("jdbcTemplate update result :" + result);
        }


    }

    @Override
    public List<Car> AvailableCars(List<Car> cars) {
        List<String> regNumbers = cars.stream()
                .map(Car::getRegNumber)
                .toList();

        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        String sql = """
                    SELECT reg_number, rental_price_per_day, brand, is_electric
                    FROM car c
                    WHERE c.reg_number IN (:cars)
                    AND NOT EXISTS (
                    SELECT 1 FROM booking b
                    WHERE b.car_id = c.reg_number
                )
                """;

        Map<String, Object> params = Map.of("cars", regNumbers);
        return namedJdbcTemplate.query(sql, params, carRowMapper);

    }

    @Override
    public void deleteBooking(UUID id) {
        var sql = """
                DELETE
                FROM booking
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, id);
        System.out.println("jdbcTemplate update result :" + result);
    }

    @Override
    public List<Booking> ViewAllUserBooking(UUID id) {
        var sql = """
                SELECT
                b.id AS booking_id,
        
                c.reg_number,
                c.rental_price_per_day,
                c.brand,
                c.is_electric,
                
                u.id AS user_id,
                u.name,
                u.password
                
                FROM booking b
                JOIN car c ON b.car_id = c.reg_number
                JOIN user_info u ON b.user_id = u.id
                WHERE b.user_id = ?
                """;

        return jdbcTemplate.query(sql, bookingRowMapper, id);

    }

    @Override
    public void updateBooking(Booking booking) {
        if (booking.getCars() != null){
            var sql = """
                   UPDATE car SET rental_price_per_day = ?, brand = ?, is_electric = ?
                   WHERE reg_number = ?
                   """;
            jdbcTemplate.update(sql, booking.getCars().getRentalPricePerDay(), booking.getCars().getBrand().name(), booking.getCars().isElectric(), booking.getCars().getRegNumber());
        }

        if (booking.getUsers() != null){
            var sql = """
                    UPDATE user_info SET name = ?
                    WHERE id = ?
                    """;
            jdbcTemplate.update(sql, booking.getUsers().getName(), booking.getUsers().getId());
        }
    }

    @Override
    public Optional<Booking> findBookingById(UUID id) {
        var sql = """
        SELECT\s
            b.id AS booking_id,
        
            c.reg_number,
            c.rental_price_per_day,
            c.brand,
            c.is_electric,
        
            u.id AS user_id,
            u.name,
            u.password
        
        FROM booking b
        JOIN car c ON b.car_id = c.reg_number
        JOIN user_info u ON b.user_id = u.id
        WHERE b.id = ?
        """;

        return jdbcTemplate.query(sql, bookingRowMapper, id).stream().findFirst();
    }
}
