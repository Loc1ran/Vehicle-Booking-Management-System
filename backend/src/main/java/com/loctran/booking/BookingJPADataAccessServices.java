package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.Exception.ResourceNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("bookingJPA")
public class BookingJPADataAccessServices implements BookingDAO{
    private final BookingRepository bookingRepository;

    public BookingJPADataAccessServices(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    @Override
    public List<Booking> ViewBooking() {
        Page<Booking> page = bookingRepository.findAll(Pageable.ofSize(100));
        return page.getContent();
    }

    @Override
    public void Booking(Booking carBooking){
       bookingRepository.save(carBooking);
    }

    @Override
    public List<Car> AvailableCars(List<Car> cars) {
        return bookingRepository.findAvailableCarsFromList(cars);
    }

    @Override
    public void deleteBooking(UUID id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> ViewAllUserBooking(UUID id) {
       return bookingRepository.findBookingsByUserId(id);
    }

    @Override
    public void updateBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> findBookingById(UUID id) {
        return bookingRepository.findById(id);
    }

}
