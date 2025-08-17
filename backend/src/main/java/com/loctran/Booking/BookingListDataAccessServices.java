package com.loctran.Booking;
import com.loctran.Car.Car;
import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Repository("bookingList")
public class BookingListDataAccessServices implements BookingDAO {
    private static final List<Booking> bookings;

    static{
        bookings = new ArrayList<>();
    }

    @Override
    public void Booking(Booking carBooking){
        bookings.add(carBooking);
    }

    @Override
    public List<Car> AvailableCars(List<Car> cars) {
        if (cars.isEmpty()) {
            return Collections.emptyList();
        }

        List<Booking> carBookings = ViewBooking();

        if (carBookings.isEmpty()) {
            return cars;
        }

        return cars.stream().filter(c -> carBookings.stream()
                .noneMatch(b -> b != null && b.getCars().equals(c))).collect(Collectors.toList());
    }

    @Override
    public void deleteBooking(UUID id) {

    }

    @Override
    public List<Booking> ViewAllUserBooking(UUID id) {
        List<Booking> bookingList = ViewBooking();
        if ( bookingList.isEmpty()){
            throw new ResourceNotFound("no booking available");
        }

        return bookingList.stream().filter(booking -> booking != null && booking.getUsers().
                getId().equals(id)).collect(Collectors.toList()); }

    @Override
    public void updateBooking(Booking booking) {

    }

    @Override
    public Optional<Booking> findBookingById(UUID id) {
        return null;
    }

    @Override
    public List<Booking> ViewBooking(){
        return bookings;
    }

}
