package com.loctran.Car;

import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import com.loctran.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServicesTest {
    private CarServices underTest;
    @Mock
    private CarDAO carDAO;


    @BeforeEach
    void setUp() {
        underTest = new CarServices(carDAO);
    }

    @Test
    void saveCar() {
        Car car = new Car("1111", new BigDecimal("100"), Brand.MERCEDES, false);

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.saveCar(car);

        verify(carDAO).saveCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(car.getRegNumber());
        assertThat(captured.getBrand()).isEqualTo(car.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(car.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(car.isElectric());
    }

    @Test
    void getAllCars() {
        Car car = new Car("1111", new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCars()).thenReturn(List.of(car));

        List<Car> actual = underTest.getAllCars();

        assertThat(actual).isEqualTo(List.of(car));
        verify(carDAO).getCars();
    }

    @Test
    void getCar() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.of(car));

        Car actual = underTest.getCar(regNumber);

        assertThat(actual.getRegNumber()).isEqualTo(regNumber);
        verify(carDAO).getCarById(regNumber);
    }

    @Test
    void willThrowAnExceptionWhenGetCarHaveNotBeenRegistered(){
        String regNumber = "1111";

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCar(regNumber)).isInstanceOf(ResourceNotFound.class)
                .hasMessage(String.format("regNumber %s not found", regNumber));
    }

    @Test
    void getElectricCars() {
        Car car1 = new Car("1111", new BigDecimal("100"), Brand.MERCEDES, false);
        Car car2 = new Car("2222", new BigDecimal("100"), Brand.TESLA, true);

        List<Car> cars = List.of(car1, car2);

        when(carDAO.getCars()).thenReturn(cars);

        List<Car> actual = underTest.getElectricCars();

        List<Car> expected = List.of(car2);

        assertThat(actual).isEqualTo(expected);
        verify(carDAO).getCars();
    }

    @Test
    void deleteCar() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.of(car));

        underTest.deleteCar(regNumber);

        verify(carDAO).getCarById(regNumber);
        verify(carDAO).deleteCar(regNumber);
    }

    @Test
    void willThrowAnExceptionWhenNoCarToDelete(){
        String regNumber = "1111";

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.deleteCar(regNumber)).isInstanceOf(ResourceNotFound.class)
                .hasMessage(String.format("car with regNumber %s not found", regNumber));
    }

    @Test
    void updateCar() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        Car updateCar = new Car(regNumber, new BigDecimal("100"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                updateCar.getRegNumber(), updateCar.getRentalPricePerDay(), updateCar.getBrand(), updateCar.isElectric()
        );

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.updateCar(regNumber, updateCarRequest);

        verify(carDAO).updateCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(regNumber);
        assertThat(captured.getBrand()).isEqualTo(updateCar.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(updateCar.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(updateCar.isElectric());


    }

    @Test
    void onlyUpdateRentalPricePerDay() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        Car updateCar = new Car(regNumber, new BigDecimal("1000"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                null, updateCar.getRentalPricePerDay(), null, car.isElectric()
        );

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.updateCar(regNumber, updateCarRequest);

        verify(carDAO).updateCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(regNumber);
        assertThat(captured.getBrand()).isEqualTo(car.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(updateCar.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(car.isElectric());
    }

    @Test
    void onlyUpdateBrand() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        Car updateCar = new Car(regNumber, new BigDecimal("1000"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                null, null, updateCar.getBrand(), car.isElectric()
        );

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.updateCar(regNumber, updateCarRequest);

        verify(carDAO).updateCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(regNumber);
        assertThat(captured.getBrand()).isEqualTo(updateCar.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(car.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(car.isElectric());
    }

    @Test
    void onlyUpdateElectric() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        Car updateCar = new Car(regNumber, new BigDecimal("1000"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                null, null, null, updateCar.isElectric()
        );

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.updateCar(regNumber, updateCarRequest);

        verify(carDAO).updateCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(regNumber);
        assertThat(captured.getBrand()).isEqualTo(car.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(car.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(updateCar.isElectric());
    }

    @Test
    void willThrownAnExceptionWhenNoUpdateCar() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                null, null, null, car.isElectric()
        );

       assertThatThrownBy(() -> underTest.updateCar(regNumber, updateCarRequest))
               .isInstanceOf(RequestValidationException.class)
               .hasMessage("no data changes found");
    }

    @Test
    void willThrownAnExceptionWhenNoCarToUpdate() {
        String regNumber = "1111";

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.empty());

        Car updateCar = new Car(regNumber, new BigDecimal("1000"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                updateCar.getRegNumber(), updateCar.getRentalPricePerDay(), updateCar.getBrand(), updateCar.isElectric()
        );

       assertThatThrownBy(() -> underTest.updateCar(regNumber, updateCarRequest))
               .isInstanceOf(ResourceNotFound.class)
               .hasMessage("car not found");
    }
}