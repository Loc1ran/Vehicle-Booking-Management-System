import com.loctran.Booking.Booking;
import com.loctran.Booking.BookingDAO;
import com.loctran.Booking.BookingServices;
import com.loctran.Car.Car;
import com.loctran.Car.CarDAO;
import com.loctran.Car.CarServices;
import com.loctran.User.User;
import com.loctran.User.UserService;

import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        BookingDAO bookingDAO = new BookingDAO();
        CarServices carServices = new CarServices();

        BookingServices bookingServices = new BookingServices(bookingDAO, carServices);
        UserService userService = new UserService();

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
                Scanner id = new Scanner(System.in);
                UUID uuid = UUID.fromString(id.next());
                ViewAllUserBookedCars(uuid, bookingServices);
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
        User[] getUser = user.getUsers();

        for( User u : getUser){
            System.out.println(u);
        }

    }

    public static void GetAvailableCars(BookingServices bookingService){
        Car[] AvailableCar = bookingService.getAvailableCars();

        for ( Car c : AvailableCar) {
            System.out.println(c);
        }
    }

    public static void GetAvailableElectricCars(BookingServices bookingService){
        Car[] AvailableEcar = bookingService.getAvailableElectricCars();
        if ( AvailableEcar.length == 0){
            System.out.println("No available Electric Cars");
        }

        for ( Car c : AvailableEcar) {
            System.out.println(c);
        }
    }

    public static void ViewBooking(BookingServices booking){
        Booking[] bookings = booking.ViewAllBooking();
        if ( bookings.length == 0){
            System.out.println("No Booked Cars");
        }
        for ( Booking b : bookings) {
            System.out.println(b);
        }
    }

    public static void ViewAllUserBookedCars(UUID id, BookingServices bookingServices){
        Booking[] AllUserBookedCars = bookingServices.ViewAllUserBooking(id);

        UserService user = new UserService();

        if ( AllUserBookedCars.length == 0 ) {
            System.out.println("user " + user.getUsersByID(id) + " has no cars booked" );
        }

        for ( Booking c : AllUserBookedCars) {
            System.out.println(c);
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