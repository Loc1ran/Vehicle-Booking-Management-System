package com.loctran.Booking;

import com.loctran.Car.Brand;
import com.loctran.Car.Car;
import com.loctran.User.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class BookingRowMapper implements RowMapper<Booking> {

    @Override
    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID bookingId = rs.getObject("booking_id", UUID.class);

        Car car = new Car();
        car.setRegNumber(rs.getString("reg_number"));
        car.setRentalPricePerDay(rs.getBigDecimal("rental_price_per_day"));
        car.setBrand(Brand.valueOf(rs.getString("brand")));
        car.setIsElectric(rs.getBoolean("is_electric"));

        User user = new User();
        user.setId(rs.getObject("user_id", UUID.class));
        user.setName(rs.getString("name"));

        return new Booking(bookingId, car, user);
    }
}
