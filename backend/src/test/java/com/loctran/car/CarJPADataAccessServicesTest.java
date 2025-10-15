package com.loctran.Car;

import com.loctran.Booking.Booking;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
        Page<Car> page = mock(Page.class);
        List<Car> cars = Arrays.asList(new Car());

        when(page.getContent()).thenReturn(cars);
        when(carRepository.findAll(any(Pageable.class))).thenReturn(page);

        // When
        List<Car> expected = underTest.getCars();

        // Then
        assertThat(expected).isEqualTo(cars);
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(carRepository).findAll(captor.capture());
        assertThat(captor.getValue()).isEqualTo(Pageable.ofSize(100));
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