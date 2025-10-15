package com.loctran.Car;

import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CarServices {
    private final CarDAO carDAO;

    public CarServices(@Qualifier("carJDBC") CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    public void saveCar(Car car){
        carDAO.saveCar(car);
    }

    public List<Car> getAllCars() {
        return carDAO.getCars();
    }

    public Car getCar(String regNumber){
        return carDAO.getCarById(regNumber).orElseThrow(()-> new ResourceNotFound(String.format("regNumber %s not found", regNumber)));
    }

    public List<Car> getElectricCars(){
        return carDAO.getCars().stream().filter(Car::isElectric).collect(Collectors.toList());
    }

    public void deleteCar(String regNumber){
        if(carDAO.getCarById(regNumber).isEmpty()){
            throw new ResourceNotFound(String.format("car with regNumber %s not found", regNumber));
        }
        carDAO.deleteCar(regNumber);
    }

    public void updateCar(String regNumber, UpdateCarRequest updateRequest){
        Car car = carDAO.getCarById(regNumber).orElseThrow(() -> new ResourceNotFound("car not found"));
        boolean changes = false;

        if( updateRequest.regNumber() != null && !updateRequest.regNumber().equals(car.getRegNumber())){
//            String oldRegNumber = car.getRegNumber();
//            String newRegNumber = updateRequest.regNumber();
//
//            car.setRegNumber(newRegNumber);
//            carDAO.deleteCar(oldRegNumber);
//            carDAO.saveCar(car);
//            changes = true;
            throw new IllegalStateException("can't change regNumber");
        }

        if( updateRequest.rentalPricePerDay() != null && !updateRequest.rentalPricePerDay().equals(car.getRentalPricePerDay())){
            car.setRentalPricePerDay(updateRequest.rentalPricePerDay());
            changes = true;
        }

        if( updateRequest.isElectric() != car.isElectric() ){
            car.setElectric(updateRequest.isElectric());
            changes = true;
        }

        if( updateRequest.brand() != null && !updateRequest.brand().equals(car.getBrand())){
            car.setBrand(updateRequest.brand());
            changes = true;
        }

        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        carDAO.updateCar(car);
    }
}

