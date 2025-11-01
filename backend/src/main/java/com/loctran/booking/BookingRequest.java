package com.loctran.booking;

import com.loctran.user.User;

import java.util.UUID;

public record BookingRequest(
        UUID userId, String regNumber
) {
}
