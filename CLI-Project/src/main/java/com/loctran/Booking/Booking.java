package com.loctran.Booking;

import com.loctran.Car.Car;
import com.loctran.User.User;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;
import java.util.UUID;

@Entity
// we need this because we learned both jdbc and jpa
@Table(
        name = "booking",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "booking_id_unique",
                        columnNames = "id"
                ),
                @UniqueConstraint(
                        name = "car_id_unique",
                        columnNames = "car_id"
                ),
                @UniqueConstraint(
                        name = "user_id_unique",
                        columnNames = "user_id"
                )

        }
)
public class Booking {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car cars;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User users;

    public Booking(UUID id, Car cars, User users) {
        this.id = id;
        this.cars = cars;
        this.users = users;
    }


    public Booking(Car cars, User users) {
        this.cars = cars;
        this.users = users;
    }

    public Booking() {

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

