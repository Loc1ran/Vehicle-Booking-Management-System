package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.User.UserDTO;

import java.util.UUID;

public record BookingDTO(
        UUID id,
        Car car,
        UserDTO user
) {
}
