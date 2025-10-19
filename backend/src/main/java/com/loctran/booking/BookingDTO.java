package com.loctran.booking;

import com.loctran.car.Car;
import com.loctran.user.UserDTO;

import java.util.UUID;

public record BookingDTO(
        UUID id,
        Car car,
        UserDTO user
) {
}
