package com.loctran.car;

import com.loctran.exception.RequestValidationException;
import com.loctran.exception.ResourceNotFound;
import com.loctran.s3.S3Buckets;
import com.loctran.s3.S3Services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServicesTest {
    private CarServices underTest;
    @Mock
    private CarDAO carDAO;
    @Mock
    private S3Services s3Services;
    @Mock
    private S3Buckets s3Buckets;


    @BeforeEach
    void setUp() {
        underTest = new CarServices(carDAO, s3Services, s3Buckets);
    }

    @Test
    void saveCar() {
        Car car = new Car("1111", new BigDecimal("100"), Brand.MERCEDES, false);

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.saveCar(car);

        verify(carDAO).saveCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(car.getRegNumber());
        assertThat(captured.getBrand()).isEqualTo(car.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(car.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(car.isElectric());
    }

    @Test
    void getAllCars() {
        Car car = new Car("1111", new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCars()).thenReturn(List.of(car));

        List<Car> actual = underTest.getAllCars();

        assertThat(actual).isEqualTo(List.of(car));
        verify(carDAO).getCars();
    }

    @Test
    void getCar() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.of(car));

        Car actual = underTest.getCar(regNumber);

        assertThat(actual.getRegNumber()).isEqualTo(regNumber);
        verify(carDAO).getCarById(regNumber);
    }

    @Test
    void willThrowAnExceptionWhenGetCarHaveNotBeenRegistered(){
        String regNumber = "1111";

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCar(regNumber)).isInstanceOf(ResourceNotFound.class)
                .hasMessage(String.format("regNumber %s not found", regNumber));
    }

    @Test
    void getElectricCars() {
        Car car1 = new Car("1111", new BigDecimal("100"), Brand.MERCEDES, false);
        Car car2 = new Car("2222", new BigDecimal("100"), Brand.TESLA, true);

        List<Car> cars = List.of(car1, car2);

        when(carDAO.getCars()).thenReturn(cars);

        List<Car> actual = underTest.getElectricCars();

        List<Car> expected = List.of(car2);

        assertThat(actual).isEqualTo(expected);
        verify(carDAO).getCars();
    }

    @Test
    void deleteCar() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.of(car));

        underTest.deleteCar(regNumber);

        verify(carDAO).getCarById(regNumber);
        verify(carDAO).deleteCar(regNumber);
    }

    @Test
    void willThrowAnExceptionWhenNoCarToDelete(){
        String regNumber = "1111";

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.deleteCar(regNumber)).isInstanceOf(ResourceNotFound.class)
                .hasMessage(String.format("car with regNumber %s not found", regNumber));
    }

    @Test
    void updateCar() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        Car updateCar = new Car(regNumber, new BigDecimal("100"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                updateCar.getRegNumber(), updateCar.getRentalPricePerDay(), updateCar.getBrand(), updateCar.isElectric()
        );

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.updateCar(regNumber, updateCarRequest);

        verify(carDAO).updateCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(regNumber);
        assertThat(captured.getBrand()).isEqualTo(updateCar.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(updateCar.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(updateCar.isElectric());


    }

    @Test
    void onlyUpdateRentalPricePerDay() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        Car updateCar = new Car(regNumber, new BigDecimal("1000"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                null, updateCar.getRentalPricePerDay(), null, car.isElectric()
        );

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.updateCar(regNumber, updateCarRequest);

        verify(carDAO).updateCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(regNumber);
        assertThat(captured.getBrand()).isEqualTo(car.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(updateCar.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(car.isElectric());
    }

    @Test
    void onlyUpdateBrand() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        Car updateCar = new Car(regNumber, new BigDecimal("1000"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                null, null, updateCar.getBrand(), car.isElectric()
        );

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.updateCar(regNumber, updateCarRequest);

        verify(carDAO).updateCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(regNumber);
        assertThat(captured.getBrand()).isEqualTo(updateCar.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(car.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(car.isElectric());
    }

    @Test
    void onlyUpdateElectric() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        Car updateCar = new Car(regNumber, new BigDecimal("1000"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                null, null, null, updateCar.isElectric()
        );

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

        underTest.updateCar(regNumber, updateCarRequest);

        verify(carDAO).updateCar(carCaptor.capture());

        Car captured = carCaptor.getValue();

        assertThat(captured.getRegNumber()).isEqualTo(regNumber);
        assertThat(captured.getBrand()).isEqualTo(car.getBrand());
        assertThat(captured.getRentalPricePerDay()).isEqualTo(car.getRentalPricePerDay());
        assertThat(captured.isElectric()).isEqualTo(updateCar.isElectric());
    }

    @Test
    void willThrownAnExceptionWhenNoUpdateCar() {
        String regNumber = "1111";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(car.getRegNumber())).thenReturn(Optional.of(car));

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                null, null, null, car.isElectric()
        );

       assertThatThrownBy(() -> underTest.updateCar(regNumber, updateCarRequest))
               .isInstanceOf(RequestValidationException.class)
               .hasMessage("no data changes found");
    }

    @Test
    void willThrownAnExceptionWhenNoCarToUpdate() {
        String regNumber = "1111";

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.empty());

        Car updateCar = new Car(regNumber, new BigDecimal("1000"), Brand.TESLA, true);

        UpdateCarRequest updateCarRequest = new UpdateCarRequest(
                updateCar.getRegNumber(), updateCar.getRentalPricePerDay(), updateCar.getBrand(), updateCar.isElectric()
        );

       assertThatThrownBy(() -> underTest.updateCar(regNumber, updateCarRequest))
               .isInstanceOf(ResourceNotFound.class)
               .hasMessage("car not found");
    }

    @Test
    void uploadCarImages() {
        String regNumber = "1234";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);
        when(carDAO.getCarById(regNumber)).thenReturn(Optional.of(car));

        byte[] bytes = "test".getBytes();
        MultipartFile multipartFile = new MockMultipartFile("file",
                bytes
        );

        String bucket = "car";
        when(s3Buckets.getCar()).thenReturn(bucket);

        underTest.uploadCarImages(regNumber, multipartFile);

        ArgumentCaptor<String> carImageId = ArgumentCaptor.forClass(String.class);

        verify(carDAO).updateCarImage(carImageId.capture(), eq(regNumber));
        verify(s3Services).putObject(
                bucket,
                "car-images/%s/%s".formatted(regNumber, carImageId.getValue()),
                bytes);

    }

    @Test
    void willThrowAnExceptionWhenCarIsEmpty(){
        String regNumber = "123";
        when(carDAO.getCarById(regNumber)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.uploadCarImages(regNumber, mock(MultipartFile.class)))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage(String.format("car with regNumber %s not found", regNumber));

        verifyNoInteractions(s3Buckets);
        verifyNoInteractions(s3Services);
        verifyNoMoreInteractions(carDAO);
    }

    @Test
    void willThrowAnExceptionWhenUploadCarImages() throws IOException {
        String regNumber = "1234";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);
        when(carDAO.getCarById(regNumber)).thenReturn(Optional.of(car));

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(multipartFile.getBytes()).thenThrow(IOException.class);

        String bucket = "car";
        when(s3Buckets.getCar()).thenReturn(bucket);

        assertThatThrownBy(() -> underTest.uploadCarImages(regNumber, multipartFile))
                .isInstanceOf(RuntimeException.class)
                        .hasRootCauseInstanceOf(IOException.class);

        verify(carDAO, never()).updateCarImage(any(), any());

    }

    @Test
    void getCarImages(){
        String regNumber = "1234";
        String carImageId = UUID.randomUUID().toString();
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false, carImageId);

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.of(car));
        String bucket = "car";
        when(s3Buckets.getCar()).thenReturn(bucket);

        when(s3Services.getObject(s3Buckets.getCar(), "car-images/%s/%s".formatted(regNumber, carImageId))).thenReturn(carImageId.getBytes());

        byte[] expected = underTest.getCarImages(regNumber);

        assertThat(carImageId.getBytes()).isEqualTo(expected);
    }

    @Test
    void willThrowAnExceptionWhenGetCarImagesIdNotExisted(){
        String regNumber = "1234";
        Car car = new Car(regNumber, new BigDecimal("100"), Brand.MERCEDES, false);

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.of(car));

        assertThatThrownBy(() -> underTest.getCarImages(regNumber))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("car image not found");

        verifyNoInteractions(s3Buckets);
        verifyNoInteractions(s3Services);
    }

    @Test
    void willThrownAnExceptionWhenCarNotExistedForGetCarImages(){
        String regNumber = "1234";

        when(carDAO.getCarById(regNumber)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCarImages(regNumber))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("car not found");

        verifyNoInteractions(s3Buckets);
        verifyNoInteractions(s3Services);
    }
}