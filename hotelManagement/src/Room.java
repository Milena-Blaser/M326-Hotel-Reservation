import java.util.Objects;

public class Room {

    private final String roomNumber;
    private final Double price;
    private final RoomType enumeration;

    public Room(final String roomNumber, final Double price, final RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }

    public String getRoomNumber() {
        return this.roomNumber;
    }

    public String toString() {
        return "\n------------------------\n Room Number: " + this.roomNumber
                + "\n Price: $" + this.price
                + "\n Enumeration: " + this.enumeration+"\n------------------------\n";
    }

    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(!(obj instanceof final Room room)) {
            return false;
        }

        return Objects.equals(this.roomNumber, room.roomNumber);
    }

    public int hashCode() {
        return Objects.hash(roomNumber);
    }
}
