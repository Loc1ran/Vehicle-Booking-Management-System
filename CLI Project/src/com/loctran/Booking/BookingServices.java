package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.Car.CarServices;
import com.loctran.User.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

       return cars.stream().filter(c -> c.getRegNumber().equals(regNumber)).findFirst().map(car -> {
           Car getcar = carServices.getCar(regNumber); UUID id = UUID.randomUUID();
           bookingDAO.Booking(new Booking(id, getcar, user));
           return id;
       }).orElseThrow(() -> new IllegalStateException("Already Booked with Registration number " + regNumber));
    }

    public List<Booking> ViewAllUserBooking(UUID id){
        List<Booking> carBooking = bookingDAO.ViewBooking();

        List<Booking> UserBookedCar =  carBooking.stream().
                filter(booking -> booking != null && booking.getUsers().
                getId().equals(id)).collect(Collectors.toList());

        return UserBookedCar;
    }

    public List<Booking> ViewAllBooking(){

        return bookingDAO.ViewBooking().stream().filter(booking -> booking != null)
                .collect(Collectors.toList());
    }

    public List<Car> getAvailableCars(){
        return getCars(carServices.getAllCars());
    }

    public List<Car> getAvailableElectricCars(){return getCars(carServices.getElectricCars()); }

    private List<Car> getCars(List<Car> cars) {
        if (cars.isEmpty()) {
            return Collections.emptyList();
        }

        List<Booking> carBookings = bookingDAO.ViewBooking();

        if (carBookings.isEmpty()) {
            return cars;
        }

        List<Car> availableCars = cars.stream().filter(c -> carBookings.stream().noneMatch(b -> b != null && b.getCars().equals(c))).collect(Collectors.toList());

        return availableCars;
    }

}