
package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.Car.CarServices;
import com.loctran.Car.UpdateCarRequest;
import com.loctran.Exception.DuplicateResourceException;
import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import com.loctran.User.UpdateUserRequest;
import com.loctran.User.User;
import com.loctran.User.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingServices {
    private final BookingDAO bookingDAO;
    private final CarServices carServices;
    private final UserService userService;


    public BookingServices(@Qualifier("bookingJDBC") BookingDAO bookingDAO, CarServices carServices, UserService userService) {
        this.bookingDAO = bookingDAO;
        this.carServices = carServices;
        this.userService = userService;
    }

    public void Book(User user, String regNumber){
        List<Car> cars = getAvailableCars();
         cars.stream().filter(c -> c.getRegNumber().equals(regNumber)).findFirst().map(car -> {
            Car getcar = carServices.getCar(regNumber);
            // void for JPA because I have auto generated UUID
            UUID id = UUID.randomUUID();
            bookingDAO.Booking(new Booking(getcar, user));
            return id;
        }).orElseThrow(() -> new IllegalStateException("Cars not found or have been registered  " + regNumber));
    }

    public List<Booking> ViewAllUserBooking(UUID id){
        return bookingDAO.ViewAllUserBooking(id);
    }

    public List<Car> getAvailableElectricCars(){
        return getCars(carServices.getElectricCars());
    }

    public List<Car> getAvailableCars(){
        return getCars(carServices.getAllCars());
    }

    private List<Car> getCars(List<Car> cars) {
        if(bookingDAO.AvailableCars(cars).isEmpty()){
            throw new DuplicateResourceException("No cars available");
        }
        return bookingDAO.AvailableCars(cars);
    }

    public List<Booking> viewAllBooking(){
        return bookingDAO.ViewBooking();
    }

    public void deleteBooking(UUID id){
        bookingDAO.deleteBooking(id);
    }

    public void updateBooking(UUID uuid, UpdateBookingRequest bookingRequest, UpdateCarRequest updateCarRequest, UpdateUserRequest updateUserRequest){
        Booking booking = bookingDAO.findBookingById(uuid).orElseThrow(() -> new ResourceNotFound("Booking not found"));
        boolean changes = false;

        if( bookingRequest.id() != null && !bookingRequest.id().equals(booking.getId()) ){
            throw new RequestValidationException("Id can't be changed");
        }

        User user = booking.getUsers();
        Car car = booking.getCars();

        if (updateUserRequest != null && updateUserRequest.hasChanges(user)){
            UUID userId = booking.getUsers().getId();
            userService.updateUser(userId, updateUserRequest);
            booking.setUsers(userService.getUsersByID(userId));
            changes = true;
        }

        if (updateCarRequest != null && updateCarRequest.hasChanges(car)) {
            String regNumber = booking.getCars().getRegNumber();
            carServices.updateCar(regNumber, updateCarRequest);
            Car updatedCar = carServices.getCar(updateCarRequest.regNumber());
            booking.setCars(updatedCar);
            changes = true;
        }

        if (!changes){
            throw new RequestValidationException("No data changes found");
        }


        bookingDAO.updateBooking(booking);
    }
}