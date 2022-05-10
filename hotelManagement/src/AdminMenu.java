import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getSingleton();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * This method handles the admin actions
     */
    public static void adminMenu() {
        String line;

        try {
            do {
                printMenu();
                line = scanner.nextLine();

                if (line.length() == 1) {
                    switch (line.charAt(0)) {
                        case '1' -> displayAllCustomers();
                        case '2' -> displayAllRooms();
                        case '3' -> displayAllReservations();
                        case '4' -> addRoom();
                        case '5' -> MainMenu.printMainMenu();
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

    /**
     * This method prints the admin menu
     * which shows his actions as an admin
     */
    private static void printMenu() {
        System.out.print("""

                Admin Menu
                --------------------------------------------
                1. See all Customers
                2. See all Rooms
                3. See all Reservations
                4. Add a Room
                5. Back to Main Menu
                --------------------------------------------
                Please select a number for the menu option:
                """);
    }

    /**
     * This method asks for information about
     * a room and adds it to the list.
     */
    private static void addRoom() {

        System.out.println("Enter room number:");
        final String roomNumber = scanner.nextLine();

        System.out.println("Enter price per night:");
        final double roomPrice = enterRoomPrice();

        System.out.println("Enter room type: 1 for single bed, 2 for double bed:");
        final RoomType roomType = enterRoomType();

        final Room room = new Room(roomNumber, roomPrice, roomType);

        adminResource.addRoom(Collections.singletonList(room));
        System.out.println("Room added successfully!");

        System.out.println("Would like to add another room? Y/N");
        addAnotherRoom();
    }

    /**
     * This method ask for the price of
     * the room.
     * @return price of the room
     */
    private static double enterRoomPrice() {
        Scanner scanner = new Scanner(System.in);
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exp) {
            System.out.println("Invalid room price! Please, enter a valid double number. " +
                    "Decimals should be separated by point (.)");
            return enterRoomPrice();
        }
    }

    /**
     * This gets the type of the bed
     * for the room that you want to create.
     * @return room type
     */
    private static RoomType enterRoomType() {
        try {
            return RoomType.valueOfLabel(scanner.nextLine());
        } catch (IllegalArgumentException exp) {
            System.out.println("Invalid room type! Please, choose 1 for single bed or 2 for double bed:");
            return enterRoomType();
        }
    }

    /**
     * This method checks if/ ask the user
     * wants to add another room and calls
     * the method to do that.
     */
    private static void addAnotherRoom() {

        try {
            String anotherRoom;

            anotherRoom = scanner.nextLine();

            while ((anotherRoom.charAt(0) != 'Y' && anotherRoom.charAt(0) != 'N')
                    || anotherRoom.length() != 1) {
                System.out.println("Please enter Y (Yes) or N (No)");
                anotherRoom = scanner.nextLine();
            }

            if (anotherRoom.charAt(0) == 'Y') {
                addRoom();
            } else if (anotherRoom.charAt(0) == 'N') {
                printMenu();
            } else {
                addAnotherRoom();
            }
        } catch (StringIndexOutOfBoundsException ex) {
            addAnotherRoom();
        }
    }

    /**
     * This method displays all rooms
     */
    private static void displayAllRooms() {
        Collection<Room> rooms = adminResource.getAllRooms();

        if(rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            adminResource.getAllRooms().forEach(System.out::println);
        }
    }

    /**
     * This method displays all customers
     * for the admin.
     */
    private static void displayAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            adminResource.getAllCustomers().forEach(System.out::println);
        }
    }

    /**
     * This method displays all
     * reservations
     */
    private static void displayAllReservations() {
        adminResource.displayAllReservations();
    }
}
