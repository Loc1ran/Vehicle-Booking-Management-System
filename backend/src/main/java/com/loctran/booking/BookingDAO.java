package com.loctran.booking;

import com.loctran.car.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingDAO {
    List<Booking> viewBooking();
    void booking(Booking booking);
    List<Car> availableCars(List<Car> cars);
    void deleteBooking(UUID id);
    List<Booking> viewAllUserBooking(UUID id);
    void updateBooking(Booking booking);
    Optional<Booking> findBookingById(UUID id);
}
