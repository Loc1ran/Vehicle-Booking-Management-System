package com.loctran.Car;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CarServices {
    private CarDAO carDAO;

    public CarServices() {
        carDAO = new CarDAO();
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

        List<Car> electricCars = carDAO.getCars();

        if (electricCars.isEmpty()) {
            return Collections.emptyList();
        }

        for ( Car Ecar : carDAO.getCars()) {
            if ( Ecar.isElectric() ){
                electricCars.add(Ecar);
            }
        }
        return electricCars;
    }

}
