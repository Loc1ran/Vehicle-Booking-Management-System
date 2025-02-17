package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.User.User;

import java.util.UUID;

public class BookingDAO {
    private static final Booking[] bookings;

    static{
        bookings = new Booking[10];
    }

    public void Booking(Booking carBooking){
        int availableSpace = -1;
        for ( int i = 0; i < bookings.length; i++){
            if ( bookings[i] == null){
                availableSpace = i;
            }
        }

        if ( availableSpace > -1 ) {
            bookings[availableSpace] = carBooking;
            return;
        }

        Booking[] biggerCarBookings = new Booking[bookings.length + 10];

        for ( int i = 0; i < bookings.length; i++){
            biggerCarBookings[i] = bookings[i];
        }

        biggerCarBookings[bookings.length] = carBooking;

    }

    public Booking[] ViewBooking(){
        return bookings;
    }

}
