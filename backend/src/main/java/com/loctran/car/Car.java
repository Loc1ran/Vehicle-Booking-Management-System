package com.loctran.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(
        name = "car",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "car_image_unique",
                        columnNames = "carImageId"
                )
        }
)
public class Car {
    @Id
    private String regNumber;

    private BigDecimal rentalPricePerDay;

    @Enumerated(EnumType.STRING)
    private Brand brand;

    @JsonProperty("isElectric")
    private boolean isElectric;

    @Column(
            name = "car_images",
            nullable = true
    )
    private String carImageId;

    public Car(String regNumber, BigDecimal rentalPricePerDay, Brand brand, boolean isElectric, String carImageId) {
        this(regNumber, rentalPricePerDay, brand, isElectric);
        this.carImageId = carImageId;
    }

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

    public void setElectric(boolean electric) {
        this.isElectric = electric;
    }

    public String getCarImageId() {
        return carImageId;
    }

    public void setCarImageId(String carImageId) {
        this.carImageId = carImageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return isElectric == car.isElectric && Objects.equals(regNumber, car.regNumber) && Objects.equals(rentalPricePerDay, car.rentalPricePerDay) && brand == car.brand && Objects.equals(carImageId, car.carImageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber, rentalPricePerDay, brand, isElectric, carImageId);
    }

    @Override
    public String toString() {
        return "Car{" +
                "regNumber='" + regNumber + '\'' +
                ", rentalPricePerDay=" + rentalPricePerDay +
                ", brand=" + brand +
                ", isElectric=" + isElectric +
                ", carImageId='" + carImageId + '\'' +
                '}';
    }
}


