package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.Car.CarServices;
import com.loctran.User.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BookingServices {
    private final BookingDAO bookingDAO;
    private final CarServices carServices;

    public BookingServices(BookingDAO bookingDAO, CarServices carServices) {
        this.bookingDAO = bookingDAO;
        this.carServices = carServices;
    }

    public UUID Book(User user, String regNumber){
       List<Car> cars = getAvailableCars();

       if(cars.isEmpty()){
           throw new IllegalStateException("No cars available");
       }

        for ( Car c : cars ){
            if ( c.getRegNumber().equals(regNumber)){
                Car car = carServices.getCar(regNumber);
                UUID id = UUID.randomUUID();
                bookingDAO.Booking( new Booking(id, car, user));
                return id;
            }
        }
        throw new IllegalStateException("Already Booked with Registration number " + regNumber);
    }

    public List<Booking> ViewAllUserBooking(UUID id){
        List<Booking> carBooking = bookingDAO.ViewBooking();

        List<Booking> UserBookedCar = new ArrayList<>();


        for ( Booking b : carBooking ){
            if (b != null && b.getUsers().getId().equals(id)){
                UserBookedCar.add(b);
            }
        }
        return UserBookedCar;
    }

    public List<Booking> ViewAllBooking(){
        List<Booking> carBooking = bookingDAO.ViewBooking();

        for ( Booking b : carBooking ){
            if (b != null){
               carBooking.add(b);
            }
        }
        return carBooking;
    }

    public List<Car> getAvailableCars(){
        return getCars(carServices.getAllCars());
    }

    public List<Car> getAvailableElectricCars(){
        return getCars(carServices.getElectricCars());
    }

    private List<Car> getCars(List<Car> cars){
        List<Booking> carBooking = bookingDAO.ViewBooking();

        if(carBooking.isEmpty())
            return Collections.emptyList();

        List<Car> availableCar = carServices.getAllCars();

        if(availableCar.isEmpty()){
            return Collections.emptyList();
        }

        for (Car c : cars) {
            boolean booked = false;

            for (Booking b : carBooking) {
                if (b == null || !b.getCars().equals(c)) {
                    continue;
                }
                booked = true;
            }
            if (!booked) {
                cars.add(c);
            }
        }
        return availableCar;
    }

}