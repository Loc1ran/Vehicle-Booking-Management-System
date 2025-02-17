package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.User.User;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Booking {
    private UUID id;
    private Car cars;
    private User users;

    public Booking(UUID id, Car cars, User users) {
        this.id = id;
        this.cars = cars;
        this.users = users;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Car getCars() {
        return cars;
    }

    public void setCars(Car cars) {
        this.cars = cars;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(cars, booking.cars) && Objects.equals(users, booking.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cars, users);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", cars=" + cars +
                ", users=" + users +
                '}';
    }
}
