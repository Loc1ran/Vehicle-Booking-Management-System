package com.loctran.car;

import java.math.BigDecimal;

public record UpdateCarRequest(
        String regNumber,
        BigDecimal rentalPricePerDay,
        Brand brand,
        boolean isElectric) {
    public boolean hasChanges(Car car) {
        return (regNumber() != null && !regNumber().equals(car.getRegNumber())) ||
                (rentalPricePerDay() != null && !rentalPricePerDay().equals(car.getRentalPricePerDay())) ||
                (brand() != null && !brand().equals(car.getBrand())) ||
                (isElectric() != car.isElectric());
    }
}
