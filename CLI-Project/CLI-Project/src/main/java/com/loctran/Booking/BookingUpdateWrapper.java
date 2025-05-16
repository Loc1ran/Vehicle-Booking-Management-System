package com.loctran.Booking;

import com.loctran.Car.UpdateCarRequest;
import com.loctran.User.UpdateUserRequest;

public record BookingUpdateWrapper(
        UpdateBookingRequest booking,
        UpdateCarRequest cars,
        UpdateUserRequest users
) {}