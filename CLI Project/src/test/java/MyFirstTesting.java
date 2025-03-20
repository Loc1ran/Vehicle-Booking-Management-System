import com.loctran.Booking.Booking;
import com.loctran.Booking.BookingDAO;
import com.loctran.Booking.BookingServices;
import com.loctran.Car.CarDAO;
import com.loctran.Car.CarServices;
import com.loctran.User.User;
import com.loctran.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;
import java.util.UUID;

public class MyFirstTesting {
    private UserService userService;
    private BookingServices bookingServices;
    private BookingDAO bookingDAO;

    @BeforeEach
    void setUp() {
        bookingDAO = new BookingDAO();
        CarDAO carDAO = new CarDAO();
        CarServices carServices = new CarServices(carDAO);
        bookingServices = new BookingServices(bookingDAO, carServices);
    }

    @Test
    void testBookingSuccess(){

        User user = new User(UUID.randomUUID(), "Loc Tran");

        UUID id = bookingServices.Book(user, "5678");

        assertNotNull(id);
    }

    @Test
    void testBookingFailure(){
        User user = new User(UUID.randomUUID(), "Loc Tran");

        bookingServices.Book(user, "4567");
        Exception exception = assertThrows(Exception.class, ()-> bookingServices.Book(user, "4567"));

        assertEquals("Already Booked with Registration number 4567", exception.getMessage());
    }

    @Test
    void testViewAllUserBooking(){
        UUID id = UUID.randomUUID();
        User user = new User(id, "Loc Tran");
        bookingServices.Book(user, "1234");

        List<Booking> userBookings = bookingServices.ViewAllUserBooking(id);


        assertEquals(1, userBookings.size());
        assertEquals(id, userBookings.getFirst().getUsers().getId());
    }

    @Test
    void testNoCarAvailable(){
        UUID id = UUID.randomUUID();
        User user = new User(id, "Loc Tran");
        bookingServices.Book(user, "1234");
        bookingServices.Book(user, "4567");
        bookingServices.Book(user, "5678");

        Exception exception = assertThrows(Exception.class, () -> bookingServices.Book(user, "4567"));

        assertEquals("No cars available", exception.getMessage());

    }


}
