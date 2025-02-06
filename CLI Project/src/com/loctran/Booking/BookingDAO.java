package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.User.User;

import java.util.UUID;

public class BookingDAO {
    private static final Booking[] bookings;
    private static int nextAvailableSpace;
    private static final int capacity = 10;

    static{
        bookings = new Booking[capacity];
    }

    public void Booking(Booking carBooking){
        if ( nextAvailableSpace+1 >= capacity ){
            System.out.println("The Booking is Full");
        }
        bookings[nextAvailableSpace++] = carBooking;
    }

    public Booking[] ViewBooking(){
        return bookings;
    }

}
