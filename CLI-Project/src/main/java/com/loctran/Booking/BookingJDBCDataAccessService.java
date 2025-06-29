package com.loctran.Booking;

import com.loctran.Car.Car;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository("bookingJDBC")
public class BookingJDBCDataAccessService implements BookingDAO{

    private final JdbcTemplate jdbcTemplate;
    private final BookingRowMapper bookingRowMapper;

    public BookingJDBCDataAccessService(JdbcTemplate jdbcTemplate, BookingRowMapper bookingRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookingRowMapper = bookingRowMapper;
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
            u.name
        
        FROM booking b
        JOIN car c ON b.car_id = c.reg_number
        JOIN user_info u ON b.user_id = u.id
        """;

        return jdbcTemplate.query(sql, bookingRowMapper);
    }

    @Override
    public void Booking(Booking booking) {
        var sql = """
                INSERT INTO Booking (id, car_id, user_id)
                VALUES (?, ?, ?)
                """;
        int result = jdbcTemplate.update(sql, booking.getId(), booking.getCars(), booking.getUsers());
        System.out.println("jdbcTemplate update result :" + result);

    }

    @Override
    public List<Car> AvailableCars(List<Car> cars) {
//        var sql = """
//                SELECT *
//                FROM car c
//                WHERE c.reg_number IN (:cars)
//                AND NOT EXISTS(
//                SELECT 1 FROM booking b
//                WHERE b.car_id = c.reg_number
//                )
//                """;
//
//        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Car.class));
        return List.of();
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
                u.name
                
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
            jdbcTemplate.update(sql, booking.getCars(), booking.getCars().getRegNumber());
        }

        if (booking.getUsers() != null){
            var sql = """
                    UPDATE user_info SET name = ?
                    WHERE id = ?
                    """;
            jdbcTemplate.update(sql, booking.getUsers(), booking.getUsers().getId());
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
            u.name
        
        FROM booking b
        JOIN car c ON b.car_id = c.reg_number
        JOIN user_info u ON b.user_id = u.id
        WHERE b.id = ?
        """;

        return jdbcTemplate.query(sql, bookingRowMapper, id).stream().findFirst();
    }
}
