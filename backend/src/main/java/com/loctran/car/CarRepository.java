package com.loctran.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CarRepository extends JpaRepository<Car, String> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Car c SET c.carImageId = ?1 WHERE c.regNumber = ?2")
    void updateImageId(String imageId, String regNumber);

}
