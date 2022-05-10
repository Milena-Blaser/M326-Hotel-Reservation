import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class HotelResource {

    private static final HotelResource SINGLETON = new HotelResource();

    private final CustomerService customerService = CustomerService.getSingleton();
    private final ReservationService reservationService = ReservationService.getSingleton();

    private HotelResource() {}

    public static HotelResource getSingleton() {
        return SINGLETON;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    /**
     * Adds customer to the customer list
     * @param email of the user
     * @param firstName of the user
     * @param lastName of the user
     */
    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public Room getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    /**
     * This method books the room.
     * @param customerEmail
     * @param room which is booked
     * @param checkInDate date of the checkin
     * @param checkOutDate date of the checkout
     * @return reservation of the room
     */
    public Reservation bookARoom(String customerEmail, Room room, Date checkInDate, Date checkOutDate) {
        return reservationService.reserveARoom(getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }

    /**
     * This methods gets the reservation
     * of the especific user
     * @param customerEmail user's mail
     * @return all reservations of the user
     */
    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        final Customer customer = getCustomer(customerEmail);

        if (customer == null) {
            return Collections.emptyList();
        }

        return reservationService.getCustomersReservation(getCustomer(customerEmail));
    }

    public Collection<Room> findARoom(final Date checkIn, final Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }

    public Collection<Room> findAlternativeRooms(final Date checkIn, final Date checkOut) {
        return reservationService.findAlternativeRooms(checkIn, checkOut);
    }

    public Date addDefaultPlusDays(final Date date) {
        return reservationService.addDefaultPlusDays(date);
    }
}
