package com.loctran.Car;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("carJDBC")
public class CarJDBCDataAccessService implements CarDAO{

    private final JdbcTemplate jdbcTemplate;
    private final CarRowMapper carRowMapper;

    public CarJDBCDataAccessService(JdbcTemplate jdbcTemplate, CarRowMapper carRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.carRowMapper = carRowMapper;
    }

    @Override
    public List<Car> getCars() {
        var sql = """
                SELECT *
                FROM car
                LIMIT 100;
                """;


        return jdbcTemplate.query(sql, carRowMapper);
    }

    @Override
    public Optional<Car> getCarById(String regNumber) {
        var sql = """
                SELECT *
                FROM car c
                WHERE c.reg_number = ?;
                """;
        return jdbcTemplate.query(sql, carRowMapper, regNumber).stream().findFirst();
    }

    @Override
    public void saveCar(Car car) {
        var sql = """
                INSERT INTO car(reg_number, rental_price_per_day, brand, is_electric)
                VALUES (?, ?, ?, ?);
                """;
        jdbcTemplate.update(sql, car.getRegNumber(), car.getRentalPricePerDay(), car.getBrand().name(), car.isElectric());
    }

    @Override
    public void deleteCar(String regNumber) {
        var sql = """
                DELETE FROM car
                WHERE reg_number = ?;
                """;
        jdbcTemplate.update(sql, regNumber);
    }

    @Override
    public void updateCar(Car car) {
        if ( car.getRentalPricePerDay() != null ) {
            var sql = """
                    UPDATE car SET rental_price_per_day = ?
                    WHERE reg_number = ?;
                    """;
            jdbcTemplate.update(sql, car.getRentalPricePerDay(), car.getRegNumber());
        }

        if ( car.getBrand() != null ) {
            var sql = """
                    UPDATE car SET brand = ?
                    WHERE reg_number = ?;
                    """;
            jdbcTemplate.update(sql, car.getBrand().name(), car.getRegNumber());
        }

            var sql = """
                    UPDATE car SET is_electric = ?
                    WHERE reg_number = ?;
                    """;
            jdbcTemplate.update(sql, car.isElectric(), car.getRegNumber());

    }
}
