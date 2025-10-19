package com.loctran.car;

import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarRepositoryTest {

    @Autowired
    private CarRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void updateImageId() {
        String regNumber = "3123";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.TESLA, true);

        underTest.save(car);

        underTest.updateImageId("2222", regNumber);

        Optional<Car> carOptional = underTest.findById(regNumber);

        assertThat(carOptional).isPresent()
                .hasValueSatisfying(
                        c -> {
                            assertThat(c.getCarImageId()).isEqualTo("2222");
                        }
                );
    }
}