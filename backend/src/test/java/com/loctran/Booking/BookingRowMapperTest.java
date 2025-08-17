package com.loctran.Booking;

import com.loctran.Car.Brand;
import com.loctran.Car.Car;
import com.loctran.User.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        UUID bookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BookingRowMapper bookingRowMapper = new BookingRowMapper();

        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("booking_id", UUID.class)).thenReturn(bookingId);
        when(rs.getString("reg_number")).thenReturn("1111");
        when(rs.getBigDecimal("rental_price_per_day")).thenReturn(new BigDecimal("100"));
        when(rs.getString("brand")).thenReturn(String.valueOf(Brand.TESLA));
        when(rs.getBoolean("is_electric")).thenReturn(Boolean.TRUE);
        when(rs.getObject("user_id", UUID.class)).thenReturn(userId);
        when(rs.getString("name")).thenReturn("Loc");

        Booking actual = bookingRowMapper.mapRow(rs, 1);

        Car car = new Car("1111", new BigDecimal("100"), Brand.TESLA, true);
        User user = new User(userId, "Loc");
        Booking expected = new Booking(
                bookingId, car, user
        );

        assertThat(actual).isEqualTo(expected);
    }
}