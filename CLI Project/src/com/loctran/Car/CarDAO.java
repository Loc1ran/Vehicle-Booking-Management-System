package com.loctran.Car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private static List<Car> cars;

    static{
        cars = new ArrayList<>();
        cars.add(new Car("1234", new BigDecimal("89.00"), Brand.TESLA, true));
        cars.add(new Car("5678", new BigDecimal("50.00"), Brand.AUDI, false));
        cars.add(new Car("5678", new BigDecimal("77.00"), Brand.MERCEDES, false));
    }

    public List<Car> getCars(){
        return cars;
    }
}
