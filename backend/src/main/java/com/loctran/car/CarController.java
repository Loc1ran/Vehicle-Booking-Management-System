package com.loctran.car;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/cars")
public class CarController {
    private final CarServices carServices;

    public CarController(CarServices carServices) {
        this.carServices = carServices;
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carServices.getAllCars();
    }

    @GetMapping("{regNumber}")
    public Car getCarById(@PathVariable("regNumber") String regNumber) {
        return carServices.getCar(regNumber);
    }

    @PostMapping
    public void addCar(@RequestBody Car car) {
         carServices.saveCar(car);
    }

    @DeleteMapping("{regNumber}")
    public void deleteCar(@PathVariable("regNumber") String regNumber) {
        carServices.deleteCar(regNumber);
    }

    @PutMapping("{regNumber}")
    public void updateCar(@PathVariable("regNumber") String regNumber, @RequestBody UpdateCarRequest updateCarRequest) {
        carServices.updateCar(regNumber, updateCarRequest);
    }

    @PostMapping(
            value = "{regNumber}/car-images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadCarImages(
            @PathVariable("regNumber") String regNumber,
            @RequestParam("file") MultipartFile file){
        carServices.uploadCarImages(regNumber, file);
    }

    @GetMapping(
            value = "{regNumber}/car-images",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getCarImages(
            @PathVariable("regNumber") String regNumber){
        return carServices.getCarImages(regNumber);
    }
}
