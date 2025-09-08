package com.loctran.Car;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Car {
    @Id
    private String regNumber;
    private BigDecimal rentalPricePerDay;
    @Enumerated(EnumType.STRING)
    private Brand brand;
    private boolean isElectric;

    public Car(String regNumber, BigDecimal rentalPricePerDay, Brand brand, boolean isElectric) {
        this.regNumber = regNumber;
        this.rentalPricePerDay = rentalPricePerDay;
        this.brand = brand;
        this.isElectric = isElectric;
    }

    public Car() {

    }

    public String getRegNumber() {
        return regNumber;
    }

    public BigDecimal getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public Brand getBrand() {
        return brand;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public void setRentalPricePerDay(BigDecimal rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setIsElectric(boolean electric) {
        isElectric = electric;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(regNumber, car.regNumber) && isElectric == car.isElectric && Objects.equals(rentalPricePerDay, car.rentalPricePerDay) && Objects.equals(brand, car.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber, rentalPricePerDay, brand, isElectric);
    }

    @Override
    public String toString() {
        return  "{regNumber=" + regNumber +
                ", rentalPricePerDay=" + rentalPricePerDay +
                ", brand='" + brand + '\'' +
                ", isElectric=" + isElectric +
                '}';
    }
}


