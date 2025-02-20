package com.loctran.Car;

import java.util.List;
import java.util.stream.Collectors;


public class CarServices {
    private final CarDAO carDAO;

    public CarServices(CarDAO carDAO) {
        this.carDAO = new CarDAO();
    }

    public List<Car> getAllCars() {
       return carDAO.getCars();
    }

    public Car getCar(String regNumber){
        for ( Car car : getAllCars()){
            if ( car.getRegNumber().equals(regNumber)) {
                return car;
            }
        }
        throw new IllegalStateException(String.format("regNumber %s not found", regNumber));
    }

    public List<Car> getElectricCars(){

        List<Car> electricCars = carDAO.getCars().stream().filter(car -> car.isElectric()).collect(Collectors.toList());

        return electricCars;
    }

}
