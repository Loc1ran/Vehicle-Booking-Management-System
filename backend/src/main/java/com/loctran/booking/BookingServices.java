
package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.Car.CarServices;
import com.loctran.Car.UpdateCarRequest;
import com.loctran.Exception.DuplicateResourceException;
import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import com.loctran.User.UpdateUserRequest;
import com.loctran.User.User;
import com.loctran.User.UserDAO;
import com.loctran.User.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.swing.text.View;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServices {
    private final BookingDAO bookingDAO;
    private final CarServices carServices;
    private final UserService userService;
    private final BookingDTOMapper bookingDTOMapper;


    public BookingServices(@Qualifier("bookingJDBC") BookingDAO bookingDAO, CarServices carServices, UserService userService, BookingDTOMapper bookingDTOMapper) {
        this.bookingDAO = bookingDAO;
        this.carServices = carServices;
        this.userService = userService;
        this.bookingDTOMapper = bookingDTOMapper;
    }

    public void Book(User user, String regNumber){
        List<Car> cars = getAvailableCars();

        cars.stream()
                .filter(c -> c.getRegNumber().equals(regNumber))
                .findFirst()
                .ifPresentOrElse(car -> {
                    Car getcar = carServices.getCar(regNumber);
                    bookingDAO.Booking(new Booking(getcar, user));
                }, () -> {
                    throw new IllegalStateException("Invalid regNumber : " + regNumber);
                });

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
        List<Car> available = bookingDAO.AvailableCars(cars);
        if (available.isEmpty()) {
            throw new ResourceNotFound("No cars available");
        }
        return available;

    }

    public List<BookingDTO> viewAllBooking(){
        return bookingDAO.ViewBooking().stream().map(bookingDTOMapper).collect(Collectors.toList());
    }

    public List<Booking> viewAllBookingEntity(){
        return bookingDAO.ViewBooking();
    }

    public void deleteBooking(UUID id){
        if (bookingDAO.findBookingById(id).isEmpty()) {
            throw new ResourceNotFound("No Booking with id: " + id);
        }
        bookingDAO.deleteBooking(id);
    }

    public BookingDTO findBookingById(UUID bookingId){
        return bookingDAO.findBookingById(bookingId).map(bookingDTOMapper)
                .orElseThrow(() -> new ResourceNotFound("No Booking with id: " + bookingId));
    }

    public Booking findBookingEntityById(UUID bookingId){
        return bookingDAO.findBookingById(bookingId)
                .orElseThrow(() -> new ResourceNotFound("No Booking with id: " + bookingId));
    }

    public void updateBooking(UUID uuid, UpdateBookingRequest bookingRequest, UpdateCarRequest updateCarRequest, UpdateUserRequest updateUserRequest){
        Booking booking = bookingDAO.findBookingById(uuid).orElseThrow(() -> new ResourceNotFound("No Booking with id: " + uuid));
        boolean changes = false;

        if( bookingRequest != null && bookingRequest.id() != null && !bookingRequest.id().equals(booking.getId()) ){
            throw new RequestValidationException("Id can't be changed");
        }

        User user = booking.getUsers();
        Car car = booking.getCars();

        if (updateUserRequest != null && updateUserRequest.hasChanges(user)){
            UUID userId = booking.getUsers().getId();
            userService.updateUser(userId, updateUserRequest);
            booking.setUsers(userService.getUserEntityByID(userId));
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