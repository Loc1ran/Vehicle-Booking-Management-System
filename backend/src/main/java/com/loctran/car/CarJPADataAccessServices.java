package com.loctran.car;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Page<Car> page = carRepository.findAll(Pageable.ofSize(100));
        return page.getContent();
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

    @Override
    public void updateCarImage(String image, String regNumber) {
        carRepository.updateImageId(image, regNumber);
    }
}
