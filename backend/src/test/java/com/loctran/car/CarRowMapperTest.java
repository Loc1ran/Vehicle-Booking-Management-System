package com.loctran.car;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CarRowMapperTest{

    @Test
    void mapRow() throws SQLException {
        CarRowMapper carRowMapper = new CarRowMapper();

        ResultSet rs = mock(ResultSet.class);

        when(rs.getString("reg_number")).thenReturn("1111");
        when(rs.getBigDecimal("rental_price_per_day")).thenReturn(new BigDecimal("100"));
        when(rs.getString("brand")).thenReturn(String.valueOf(Brand.TESLA));
        when(rs.getBoolean("is_electric")).thenReturn(Boolean.TRUE);

        Car actual = carRowMapper.mapRow(rs, 1);

        Car expected = new Car("1111", new BigDecimal("100"), Brand.TESLA, true);

        assertThat(actual).isEqualTo(expected);

    }
}