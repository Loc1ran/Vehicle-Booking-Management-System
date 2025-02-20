import com.loctran.Booking.Booking;
import com.loctran.Booking.BookingDAO;
import com.loctran.Booking.BookingServices;
import com.loctran.Car.Car;

import com.loctran.Car.CarDAO;
import com.loctran.Car.CarServices;
import com.loctran.User.User;
import com.loctran.User.UserDAO;
import com.loctran.User.UserFileDataAccess;
import com.loctran.User.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        UserDAO userDao = new UserFileDataAccess();
        UserService userService = new UserService(userDao);

        BookingDAO carBookingDao = new BookingDAO();
        CarDAO carDAO = new CarDAO();

        CarServices carService = new CarServices(carDAO);
        BookingServices bookingServices = new BookingServices(carBookingDao, carService);

        Option();
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice != 7) {
            switch (choice) {
            case 1:
                scanner.nextLine();
                Booking(userService, bookingServices, scanner);
                break;
            case 2:
                GetUser(userService);
                System.out.println("choose id");
                Scanner id = new Scanner(System.in);
                UUID uuid = UUID.fromString(id.next());
                ViewAllUserBookedCars(uuid, bookingServices, userDao);
                break;
            case 3:
                ViewBooking(bookingServices);
                break;
            case 4:
                GetAvailableCars(bookingServices);
                break;
            case 5:
                GetAvailableElectricCars(bookingServices);
                break;
            case 6:
                GetUser(userService);
                break;
            default:
                System.out.println(choice + " is not a valid option");
            }
            System.out.println("\n");
            Option();
            choice = scanner.nextInt();
        }
    }

    public static void Option() {
        System.out.println("1 - Book Car");
        System.out.println("2 - View All Booked Cars");
        System.out.println("3 - View All Bookings");
        System.out.println("4 - View Available Cars");
        System.out.println("5 - View Available Electric Cars");
        System.out.println("6 - View All Users");
        System.out.println("7 - Exit");
    }

    public static void GetUser(UserService user){

        user.getUsers().forEach(System.out::println);

    }

    public static void GetAvailableCars(BookingServices bookingService){
        List<Car> availableCars = bookingService.getAvailableCars();

        if(availableCars.isEmpty()){
            System.out.println("No cars available");
        }
        else {
            bookingService.getAvailableCars().forEach(System.out::println);
        }

    }

    public static void GetAvailableElectricCars(BookingServices bookingService){
        List<Car> AvailableEcar = bookingService.getAvailableElectricCars();
        if ( AvailableEcar.isEmpty() ){
            System.out.println("No available Electric Cars");
        }
        else{
            AvailableEcar.forEach(System.out::println);
        }

    }

    public static void ViewBooking(BookingServices booking){
        List<Booking> bookings = booking.ViewAllBooking();
        if ( bookings.isEmpty()){
            System.out.println("No Booked Cars");
        }
        else{
            bookings.forEach(System.out::println);
        }
    }

    public static void ViewAllUserBookedCars(UUID id, BookingServices bookingServices, UserDAO userDao){
        List<Booking> AllUserBookedCars = bookingServices.ViewAllUserBooking(id);

        UserService user = new UserService(userDao);

        if ( AllUserBookedCars.isEmpty() ) {
            System.out.println("user " + user.getUsersByID(id) + " has no cars booked" );
        }
        else{
            AllUserBookedCars.forEach(System.out::println);
        }
    }

    public static void Booking(UserService userService, BookingServices bookingService, Scanner scanner){

        GetAvailableCars(bookingService);
        System.out.println("➡️ select car reg number");
        String regNumber = scanner.nextLine();
        
        GetUser(userService);

        System.out.println("Select id: ");
        String id = scanner.nextLine();

        try {
            UUID userId = UUID.fromString(id);
            User user = userService.getUsersByID(userId);
            if (user == null) {
                System.out.println("no user with id " + id + " found");
            } else {
                UUID uuid = bookingService.Book(user, regNumber);
                System.out.println("booked car with regNumber " + regNumber + " for user " + user + ". Booking ref : " + uuid);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format: " + id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}