package com.loctran.Car;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("carJPA")
public class CarJPADataAccessServices implements CarDAO{
    private final CarRepository carRepository;

    public CarJPADataAccessServices(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getCars(){
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> getCarById(String regNumber ) {
        return carRepository.findById(regNumber);
    }

    public void saveCar(Car car){
        carRepository.save(car);
    }

    @Override
    public void deleteCar(String regNumber) {
        carRepository.deleteById(regNumber);
    }

    @Override
    public void updateCar(Car car) {
        carRepository.save(car);
    }


}
