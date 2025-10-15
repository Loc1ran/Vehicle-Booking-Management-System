package com.loctran.Car;

import com.loctran.AbstractDaoUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CarJDBCDataAccessServiceTest extends AbstractDaoUnitTest {
    private CarJDBCDataAccessService underTest;
    private final CarRowMapper carRowMapper = new CarRowMapper();

    @BeforeEach
    void setUp() {
         underTest =  new CarJDBCDataAccessService(
                 getJdbcTemplate(), carRowMapper
         );

    }

    @Test
    void getCars() {
        Car car = new Car("1111", new BigDecimal("1000"), Brand.TESLA, true);

        underTest.saveCar(car);

        List<Car> cars = underTest.getCars();

        assertThat(cars).isNotEmpty();
    }

    @Test
    void getCarById() {
        Car car = new Car("1234", new BigDecimal("1000.00"), Brand.TESLA, true);

        underTest.saveCar(car);

        Optional<Car> actual = underTest.getCarById("1234");

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getRegNumber()).isEqualTo(car.getRegNumber());
            assertThat(c.getRentalPricePerDay()).isEqualTo(car.getRentalPricePerDay());
            assertThat(c.getBrand()).isEqualTo(car.getBrand());
            assertThat(c.isElectric()).isEqualTo(car.isElectric());
        });
    }

    @Test
    void notReturnGetCarById(){
        String regNumber = "-1111";

        var actual = underTest.getCarById(regNumber);

        assertThat(actual).isEmpty();

    }

    @Test
    void saveCar() {
        Car car = new Car("2222", new BigDecimal("1000.00"), Brand.TESLA, true);

        underTest.saveCar(car);

        List<Car> actual = underTest.getCars();

        assertThat(actual).isNotEmpty();
    }

    @Test
    void deleteCar() {
        Car car = new Car("3333", new BigDecimal("1000.00"), Brand.TESLA, true);

        underTest.saveCar(car);

        underTest.deleteCar(car.getRegNumber());

        Optional<Car> actual = underTest.getCarById(car.getRegNumber());

        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCar() {
        Car car = new Car("4444", new BigDecimal("1000.00"), Brand.TESLA, true);

        underTest.saveCar(car);

        Brand newBrand = Brand.MERCEDES;
        BigDecimal newRentalPrice = new BigDecimal("38.21");

        Car carUpdate = new Car();
        carUpdate.setRegNumber("4444");
        carUpdate.setBrand(newBrand);
        carUpdate.setRentalPricePerDay(newRentalPrice);
        carUpdate.setElectric(false);

        underTest.updateCar(carUpdate);

        Optional<Car> actual = underTest.getCarById("4444");

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getBrand()).isEqualTo(newBrand);
            assertThat(c.getRentalPricePerDay()).isEqualTo(newRentalPrice);
            assertThat(c.isElectric()).isFalse();
        });

    }
}