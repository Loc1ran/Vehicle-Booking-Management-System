package com.loctran.Car;

import java.util.UUID;

public class CarServices {
    private CarDAO carDAO;

    public CarServices() {
        carDAO = new CarDAO();
    }

    public Car[] getAllCars() {
       return carDAO.getCars();
    }

    public Car getCar(String regNumber){
        for ( Car car : getAllCars()){
            if ( car.getRegNumber().equals(regNumber)) {
                return car;
            }
        }
        throw new IllegalStateException(String.format("regNumber not found", regNumber));
    }

    public Car[] getElectricCars(){
        int count = 0;
        for ( Car Ecar : carDAO.getCars()) {
            if ( Ecar.isElectric() ){
                count++;
            }
        }

        if ( count == 0){
            return new Car[0];
        }

        Car[] electricCars = new Car[count];
        int index = 0;

        for ( Car Ecar : carDAO.getCars()) {
            if ( Ecar.isElectric() ){
                electricCars[index++] = Ecar;
            }
        }
        return electricCars;
    }

}
