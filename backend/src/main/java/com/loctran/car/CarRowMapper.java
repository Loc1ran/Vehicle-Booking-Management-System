package com.loctran.car;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CarRowMapper implements RowMapper<Car> {

    @Override
    public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
        String regNumber = rs.getString("reg_number");
        BigDecimal price = rs.getBigDecimal("rental_price_per_day");
        Brand brand = Brand.valueOf(rs.getString("brand"));
        boolean isElectric = rs.getBoolean("is_electric");
        String carImages = rs.getString("car_images");
        return new Car(regNumber, price, brand, isElectric, carImages);
    }
}
