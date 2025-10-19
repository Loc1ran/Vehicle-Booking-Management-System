package com.loctran.booking;

import com.loctran.car.Car;
import com.loctran.user.User;

import java.util.UUID;

public record UpdateBookingRequest(UUID id, User user, Car car) {

}
