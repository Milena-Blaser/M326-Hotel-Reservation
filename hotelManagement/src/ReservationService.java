import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationService {

    private static final ReservationService SINGLETON = new ReservationService();
    private static final int RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS = 7;

    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, Collection<Reservation>> reservations = new HashMap<>();

    private ReservationService() {}

    public static ReservationService getSingleton() {
        return SINGLETON;
    }

    public void addRoom(final Room room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public Room getARoom(final String roomNumber) {
        return rooms.get(roomNumber);
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    /**
     * Reserves the room and add its to the reservations of the hotel.
     * @param customer which books the room
     * @param room which the customer wants to book
     * @param checkInDate date of the checkin
     * @param checkOutDate date of the checkout
     * @return the reservation
     */
    public Reservation reserveARoom(final Customer customer, final Room room,
                                    final Date checkInDate, final Date checkOutDate) {
        final Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);

        Collection<Reservation> customerReservations = getCustomersReservation(customer);

        if (customerReservations == null) {
            customerReservations = new LinkedList<>();
        }

        customerReservations.add(reservation);
        reservations.put(customer.getEmail(), customerReservations);

        return reservation;
    }

    /**
     * Gets room for the wished dates
     * @param checkInDate date
     * @param checkOutDate date
     * @return all rooms for this dates
     */
    public Collection<Room> findRooms(final Date checkInDate, final Date checkOutDate) {
        return findAvailableRooms(checkInDate, checkOutDate);
    }

    /**
     * Finds alternative rooms for a similar date
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    public Collection<Room> findAlternativeRooms(final Date checkInDate, final Date checkOutDate) {
        return findAvailableRooms(addDefaultPlusDays(checkInDate), addDefaultPlusDays(checkOutDate));
    }

    /**
     * Search for available rooms
     * @param checkInDate date
     * @param checkOutDate date
     * @return all rooms available
     */
    private Collection<Room> findAvailableRooms(final Date checkInDate, final Date checkOutDate) {
        final Collection<Reservation> allReservations = getAllReservations();
        final Collection<Room> notAvailableRooms = new LinkedList<>();

        for (Reservation reservation : allReservations) {
            if (reservationOverlaps(reservation, checkInDate, checkOutDate)) {
                notAvailableRooms.add(reservation.getRoom());
            }
        }

        return rooms.values().stream().filter(room -> notAvailableRooms.stream()
                .noneMatch(notAvailableRoom -> notAvailableRoom.equals(room)))
                .collect(Collectors.toList());
    }

    /**
     * Add to the date default dates
     * @param date wished date
     * @return new date (aktualisiert um paar tagen)
     */
    public Date addDefaultPlusDays(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS);

        return calendar.getTime();
    }

    /**
     * Checks if a reservation overlaps with the
     * checkin and checkout date for another reservation
     * @param reservation reservation
     * @param checkInDate date
     * @param checkOutDate date
     * @return false when it overlaps
     */
    private boolean reservationOverlaps(final Reservation reservation, final Date checkInDate,
                                        final Date checkOutDate){
        return checkInDate.before(reservation.getCheckOutDate())
                && checkOutDate.after(reservation.getCheckInDate());
    }

    /**
     * gets customer reservation
     * @param customer
     * @return
     */
    public Collection<Reservation> getCustomersReservation(final Customer customer) {
        return reservations.get(customer.getEmail());
    }

    /**
     * Displays all reservation
     */
    public void printAllReservation() {
        final Collection<Reservation> reservations = getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation + "\n");
            }
        }
    }

    /**
     * Gets all reservation
     * @return the reservation
     */
    private Collection<Reservation> getAllReservations() {
        final Collection<Reservation> allReservations = new LinkedList<>();

        for(Collection<Reservation> reservations : reservations.values()) {
            allReservations.addAll(reservations);
        }

        return allReservations;
    }
}
