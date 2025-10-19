package com.loctran.booking;

import com.loctran.car.UpdateCarRequest;
import com.loctran.user.UpdateUserRequest;

public record BookingUpdateWrapper(
        UpdateBookingRequest booking,
        UpdateCarRequest cars,
        UpdateUserRequest users
) {}