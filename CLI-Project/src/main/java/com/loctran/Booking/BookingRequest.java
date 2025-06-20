package com.loctran.Booking;

import com.loctran.User.User;

public record BookingRequest(User user, String regNumber) {
}
