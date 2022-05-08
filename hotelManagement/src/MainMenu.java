import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
public class MainMenu {

    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    private static final HotelResource hotelResource = HotelResource.getSingleton();

    public static void mainMenu() {
        String line;
        Scanner scanner = new Scanner(System.in);

        try {
            do {
                printMainMenu();
                line = scanner.nextLine();

                if (line.length() == 1) {
                    switch (line.charAt(0)) {
                        case '1' -> findAndReserveRoom();
                        case '2' -> seeMyReservation();
                        case '3' -> createAccount();
                        case '4' -> AdminMenu.adminMenu();
                        case '5' -> System.out.println("Exit");
                        default -> System.out.println("Unknown action\n");
                    }
                } else {
                    System.out.println("Error: Invalid action\n");
                }
            } while (line.charAt(0) != '5' || line.length() != 1);
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("Empty input received. Exiting program...");
        }
    }

    private static void findAndReserveRoom() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Check-In Date mm/dd/yyyy example 02/01/2022");
        Date checkIn = getInputDate(scanner);

        System.out.println("Enter Check-Out Date mm/dd/yyyy example 02/21/2022");
        Date checkOut = getInputDate(scanner);

        if (checkIn != null && checkOut != null) {
            Collection<Room> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                Collection<Room> alternativeRooms = hotelResource.findAlternativeRooms(checkIn, checkOut);

                if (alternativeRooms.isEmpty()) {
                    System.out.println("No rooms found.");
                } else {
                    final Date alternativeCheckIn = hotelResource.addDefaultPlusDays(checkIn);
                    final Date alternativeCheckOut = hotelResource.addDefaultPlusDays(checkOut);
                    System.out.println("We've only found rooms on alternative dates:" +
                            "\nCheck-In Date:" + alternativeCheckIn +
                            "\nCheck-Out Date:" + alternativeCheckOut);

                    printRooms(alternativeRooms);
                    reserveRoom(scanner, alternativeCheckIn, alternativeCheckOut, alternativeRooms);
                }
            } else {
                printRooms(availableRooms);
                reserveRoom(scanner, checkIn, checkOut, availableRooms);
            }
        }
    }

    private static Date getInputDate(final Scanner scanner) {
        try {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(scanner.nextLine());
        } catch (ParseException ex) {
            System.out.println("Error: Invalid date.");
            findAndReserveRoom();
        }

        return null;
    }

    private static void reserveRoom(final Scanner scanner, final Date checkInDate,
                                    final Date checkOutDate, final Collection<Room> rooms) {
        System.out.println("Would you like to book? y/n");
        final String bookRoom = scanner.nextLine();

        if ("y".equals(bookRoom)) {
            System.out.println("Do you have an account with us? y/n");
            final String haveAccount = scanner.nextLine();

            if ("y".equals(haveAccount)) {
                System.out.println("Enter Email format: name@domain.com");
                final String customerEmail = scanner.nextLine();

                if (hotelResource.getCustomer(customerEmail) == null) {
                    System.out.println("Customer not found.\nYou may need to create a new account.");
                } else {
                    System.out.println("What room number would you like to reserve?");
                    final String roomNumber = scanner.nextLine();

                    if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
                        final Room room = hotelResource.getRoom(roomNumber);

                        final Reservation reservation = hotelResource
                                .bookARoom(customerEmail, room, checkInDate, checkOutDate);
                        System.out.println("Reservation created successfully!");
                        System.out.println(reservation);
                    } else {
                        System.out.println("Error: room number not available.\nStart reservation again.");
                    }
                }
            } else {
                System.out.println("Please, create an account.");
            }
        } else if (!"n".equals(bookRoom)){
            reserveRoom(scanner, checkInDate, checkOutDate, rooms);
        }
    }

    private static void printRooms(final Collection<Room> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private static void seeMyReservation() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your Email format: name@domain.com");
        final String customerEmail = scanner.nextLine();

        printReservations(hotelResource.getCustomersReservations(customerEmail));
    }

    private static void printReservations(final Collection<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(reservation -> System.out.println("\n" + reservation));
        }
    }

    private static void createAccount() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Email format: name@domain.com");
        final String email = scanner.nextLine();

        System.out.println("First Name:");
        final String firstName = scanner.nextLine();

        System.out.println("Last Name:");
        final String lastName = scanner.nextLine();

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");

        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());

        }
    }

    public static void printMainMenu()
    {
        System.out.print("""

                Welcome to the Hotel Reservation Application
                --------------------------------------------
                1. Find and reserve a room
                2. See my reservations
                3. Create an Account
                4. Admin
                5. Exit
                --------------------------------------------
                Please select a number for the menu option:
                """);
    }
}
