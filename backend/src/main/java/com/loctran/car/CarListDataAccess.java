package com.loctran.Car;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository("carList")
public class CarListDataAccess implements CarDAO{
    private static List<Car> cars;
    @Override

    public void saveCar(Car car) {
        cars = Arrays.asList(
                new Car("1234", new BigDecimal("89.00"), Brand.TESLA, true),
                new Car("4567", new BigDecimal("50.00"), Brand.AUDI, false),
                new Car("5678", new BigDecimal("77.00"), Brand.MERCEDES, false));

    }

    @Override
    public void deleteCar(String regNumber) {

    }

    @Override
    public void updateCar(Car car) {

    }

    public List<Car> getCars(){
        return cars;
    }

    @Override
    public Optional<Car> getCarById(String regNumber) {
        return cars.stream().filter(car -> car.getRegNumber().equals(regNumber)).findFirst();
    }



}

