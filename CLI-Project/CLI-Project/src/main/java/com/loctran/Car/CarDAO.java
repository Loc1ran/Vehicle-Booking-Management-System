package com.loctran.Car;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarDAO {
    List<Car> getCars();
    Optional<Car> getCarById(String regNumber);
    void saveCar(Car car);
    void deleteCar(String regNumber);
    void updateCar(Car car);
}
