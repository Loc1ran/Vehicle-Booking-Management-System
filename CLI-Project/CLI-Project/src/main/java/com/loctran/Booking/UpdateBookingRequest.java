package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.User.User;

import java.util.UUID;

public record UpdateBookingRequest(UUID id, User user, Car car) {

}
