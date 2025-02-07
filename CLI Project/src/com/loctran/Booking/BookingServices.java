package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.Car.CarServices;
import com.loctran.User.User;

import java.util.UUID;

public class BookingServices {
    private final BookingDAO bookingDAO;
    private final CarServices carServices;

    public BookingServices(BookingDAO bookingDAO, CarServices carServices) {
        this.bookingDAO = bookingDAO;
        this.carServices = carServices;
    }

    public UUID Book(User user, String regNumber){
        Car[] cars = getAvailableCars();

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

    public Booking[] ViewAllUserBooking(UUID id){
        Booking[] carBooking = bookingDAO.ViewBooking();
        if ( carBooking.length == 0 ){
            return new Booking[0];
        }
        int userbookedcar = 0;

        for ( Booking b : carBooking ){
            if (b != null && b.getUsers().getId().equals(id)){
                userbookedcar++;
            }
        }

        Booking[] UserBookedCar = new Booking[userbookedcar];
        int index = 0;

        for ( Booking b : carBooking ){
            if (b != null && b.getUsers().getId().equals(id)){
                UserBookedCar[index++] = b;
            }
        }
        return UserBookedCar;
    }

    public Booking[] ViewAllBooking(){
        Booking[] carBooking = bookingDAO.ViewBooking();

        int numberOfBooking = 0;

        for ( Booking b : carBooking ){
            if (b != null){
                numberOfBooking++;
            }
        }

        Booking[] Booking = new Booking[numberOfBooking];
        int index = 0;

        for ( Booking b : carBooking ){
            if (b != null){
                Booking[index++] = b;
            }
        }
        return Booking;
    }

    public Car[] getAvailableCars(){
        return getCars(carServices.getAllCars());
    }

    public Car[] getAvailableElectricCars(){
        return getCars(carServices.getElectricCars());
    }

    private Car[] getCars(Car[] cars){
        Booking[] carBooking = bookingDAO.ViewBooking();

        if(carBooking.length == 0)
            return cars;

        int availableSlot = 0;

        for (Car c : cars) {
            boolean booked = false;

            for (Booking b : carBooking) {
                if (b == null || !b.getCars().equals(c)) {
                    continue;
                }
                booked = true;
            }
            if (!booked) {
                ++availableSlot;
            }
        }

        Car[] availableCar = new Car[availableSlot];
        int index = 0;
        for (Car c : cars) {
            boolean booked = false;

            for (Booking b : carBooking) {
                if (b == null || !b.getCars().equals(c)) {
                    continue;
                }
                booked = true;
            }
            if (!booked) {
                availableCar[index++] = c;
            }
        }
        return availableCar;
    }

}