package com.loctran.Booking;

import com.loctran.Car.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingDAO {
    List<Booking> ViewBooking();
    void Booking(Booking booking);
    List<Car> AvailableCars(List<Car> cars);
    void deleteBooking(UUID id);
    List<Booking> ViewAllUserBooking(UUID id);
    void updateBooking(Booking booking);
    Optional<Booking> findBookingById(UUID id);
}
