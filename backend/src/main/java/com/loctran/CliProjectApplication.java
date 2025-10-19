package com.loctran;

import com.loctran.booking.BookingRepository;
import com.loctran.car.CarRepository;
import com.loctran.s3.S3Buckets;
import com.loctran.user.UserRepository;
import com.loctran.s3.S3Services;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CliProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CliProjectApplication.class, args);
	}

	// For test CRUD operation without POST request
//	@Bean
//	CommandLineRunner runner(BookingRepository bookingRepository, CarRepository carRepository, UserRepository userRepository, BookingRepository bookingRespository, S3Services s3Services, S3Buckets s3Buckets) {
//		return args -> {
//			s3Services.putObject(
//					s3Buckets.getCar(),
//					"test",
//						"HelloWorld".getBytes()
//			);
//
//			byte[] tests = s3Services.getObject("fs-loctran-car-test", "test");
//
//			System.out.println(new String(tests));
//			Car car = new Car("1122", new BigDecimal("123123"), Brand.TESLA, true);
//			carRepository.save(car);
//			User user = new User("Loc");
//			userRepository.save(user);
//			bookingRespository.save(new Booking(car,user));
//
//			Car car2 = new Car("3333", new BigDecimal("1111"), Brand.MERCEDES, false);
//			carRepository.save(car2);
//			User user2 = new User("Ham");
//			userRepository.save(user2);
//			bookingRespository.save(new Booking(car2,user2));
//
//			Car car3 = new Car("4444", new BigDecimal("122211"), Brand.TESLA, true);
//			carRepository.save(car3);
//
//			Car car4 = new Car("1111", new BigDecimal("12312321"), Brand.MERCEDES, false);
//			carRepository.save(car4);
//		};
//	}
}
