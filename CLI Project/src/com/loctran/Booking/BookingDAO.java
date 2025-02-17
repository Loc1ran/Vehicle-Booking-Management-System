package com.loctran.Booking;
import java.util.ArrayList;
import java.util.List;


public class BookingDAO {
    private static final List<Booking> bookings;

    static{
        bookings = new ArrayList<>();
    }

    public void Booking(Booking carBooking){
       bookings.add(carBooking);
    }

    public List<Booking> ViewBooking(){
        return bookings;
    }

}
