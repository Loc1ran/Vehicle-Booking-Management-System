package com.loctran.Car;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CarJPADataAccessServicesTest {
    private CarJPADataAccessServices underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CarJPADataAccessServices(carRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getCars() {
        underTest.getCars();

        verify(carRepository).findAll();
    }

    @Test
    void getCarById() {
        String regNumber = "1111";

        underTest.getCarById(regNumber);

        verify(carRepository).findById(regNumber);
    }

    @Test
    void saveCar() {
        Car car = new Car("1111", new BigDecimal("100"), Brand.TESLA, true);

        underTest.saveCar(car);

        verify(carRepository).save(car);
    }

    @Test
    void deleteCar() {
        String regNumber = "1111";

        underTest.deleteCar(regNumber);

        verify(carRepository).deleteById(regNumber);
    }

    @Test
    void updateCar() {
        Car updateCar = new Car("1111", new BigDecimal("100"), Brand.MERCEDES, false);

        underTest.updateCar(updateCar);

        verify(carRepository).save(updateCar);
    }
}