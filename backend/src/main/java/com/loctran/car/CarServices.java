package com.loctran.car;

import com.loctran.exception.RequestValidationException;
import com.loctran.exception.ResourceNotFound;
import com.loctran.s3.S3Buckets;
import com.loctran.s3.S3Services;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class CarServices {
    private final CarDAO carDAO;
    private final S3Services s3Services;
    private final S3Buckets s3Buckets;

    public CarServices(@Qualifier("carJDBC") CarDAO carDAO, S3Services s3Services, S3Buckets s3Buckets) {
        this.carDAO = carDAO;
        this.s3Services = s3Services;
        this.s3Buckets = s3Buckets;
    }

    public void saveCar(Car car){
        carDAO.saveCar(car);
    }

    public List<Car> getAllCars() {
        return carDAO.getCars();
    }

    public Car getCar(String regNumber){
        return carDAO.getCarById(regNumber).orElseThrow(()-> new ResourceNotFound(String.format("regNumber %s not found", regNumber)));
    }

    public List<Car> getElectricCars(){
        return carDAO.getCars().stream().filter(Car::isElectric).collect(Collectors.toList());
    }

    public void deleteCar(String regNumber){
        if(carDAO.getCarById(regNumber).isEmpty()){
            throw new ResourceNotFound(String.format("car with regNumber %s not found", regNumber));
        }
        carDAO.deleteCar(regNumber);
    }

    public void updateCar(String regNumber, UpdateCarRequest updateRequest){
        Car car = carDAO.getCarById(regNumber).orElseThrow(() -> new ResourceNotFound("car not found"));
        boolean changes = false;

        if( updateRequest.regNumber() != null && !updateRequest.regNumber().equals(car.getRegNumber())){
//            String oldRegNumber = car.getRegNumber();
//            String newRegNumber = updateRequest.regNumber();
//
//            car.setRegNumber(newRegNumber);
//            carDAO.deleteCar(oldRegNumber);
//            carDAO.saveCar(car);
//            changes = true;
            throw new IllegalStateException("can't change regNumber");
        }

        if( updateRequest.rentalPricePerDay() != null && !updateRequest.rentalPricePerDay().equals(car.getRentalPricePerDay())){
            car.setRentalPricePerDay(updateRequest.rentalPricePerDay());
            changes = true;
        }

        if( updateRequest.isElectric() != car.isElectric() ){
            car.setElectric(updateRequest.isElectric());
            changes = true;
        }

        if( updateRequest.brand() != null && !updateRequest.brand().equals(car.getBrand())){
            car.setBrand(updateRequest.brand());
            changes = true;
        }

        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        carDAO.updateCar(car);
    }

    public void uploadCarImages(String regNumber, MultipartFile file) {
        if(carDAO.getCarById(regNumber).isEmpty()){
            throw new ResourceNotFound(String.format("car with regNumber %s not found", regNumber));
        }

        String carImageId = UUID.randomUUID().toString();

        try {
            s3Services.putObject(
                    s3Buckets.getCar(),
                    "car-images/%s/%s".formatted(regNumber, carImageId),
                    file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        carDAO.updateCarImage(carImageId, regNumber);

    }

    public byte[] getCarImages(String regNumber) {
        Car car = carDAO.getCarById(regNumber)
                .orElseThrow(()-> new ResourceNotFound("car not found"));

        String carImageId = car.getCarImageId();
        if(StringUtils.isBlank(carImageId)){
            throw new ResourceNotFound("car image not found");
        }

        return s3Services.getObject(s3Buckets.getCar(), "car-images/%s/%s".formatted(regNumber, carImageId));
    }
}

