package com.loctran.booking;

import com.loctran.car.Car;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {
    private final BookingServices bookingServices;

    public BookingController(BookingServices bookingServices) {
        this.bookingServices = bookingServices;
    }

    @GetMapping
    public List<BookingDTO> getAllBookings() {
        return bookingServices.viewAllBooking();
    }

    @GetMapping("{uuid}")
    public BookingDTO getBookingById(@PathVariable("uuid") UUID id) {
        return bookingServices.findBookingById(id);
    }

    @GetMapping("viewUserBookedCars/{uuid}")
    public List<Booking> UsersBookedCars(@PathVariable("uuid") UUID id){
        return bookingServices.ViewAllUserBooking(id);
    }

    @GetMapping("getAvailableCars")
    public List<Car> AvailableCars(){
        return bookingServices.getAvailableCars();
    }

    @GetMapping("getAvailableElectricCars")
    public List<Car> AvailableElectricCars(){
        return bookingServices.getAvailableElectricCars();
    }

    @PostMapping
    public void Booking(
            @RequestBody BookingRequest bookingRequest){
        bookingServices.Book(bookingRequest.userId(), bookingRequest.regNumber());
    }

    @DeleteMapping("{uuid}")
    public void deleteBooking(@PathVariable("uuid") UUID uuid){
        bookingServices.deleteBooking(uuid);
    }

    @PutMapping("{uuid}")
    public void updateBooking(@PathVariable("uuid") UUID uuid, @RequestBody BookingUpdateWrapper bookingRequest){
        bookingServices.updateBooking(uuid, bookingRequest.booking(), bookingRequest.cars(), bookingRequest.users());
    }
}
