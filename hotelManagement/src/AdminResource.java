import java.util.Collection;
import java.util.List;

public class AdminResource {

    private static final AdminResource SINGLETON = new AdminResource();

    private final CustomerService customerService = CustomerService.getSingleton();
    private final ReservationService reservationService = ReservationService.getSingleton();

    private AdminResource() {}

    public static AdminResource getSingleton() {
        return SINGLETON;
    }

    public void addRoom(List<Room> rooms) {
        rooms.forEach(reservationService::addRoom);
    }

    public Collection<Room> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationService.printAllReservation();
    }
}
