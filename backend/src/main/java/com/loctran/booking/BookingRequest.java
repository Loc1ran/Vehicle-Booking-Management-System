package com.loctran.booking;

import com.loctran.user.User;

public record BookingRequest(
        User user, String regNumber
) {
}
