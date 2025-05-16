package com.loctran.Booking;

import com.loctran.Car.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
     @Query("SELECT c FROM Car c WHERE c IN :cars AND c NOT IN (SELECT b.cars FROM Booking b)")
     List<Car> findAvailableCarsFromList(@Param("cars") List<Car> cars);

     @Query("SELECT b FROM Booking b WHERE b.users.id = :userId")
     List<Booking> findBookingsByUserId(@Param("userId") UUID userId);


}
