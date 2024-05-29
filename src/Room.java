// Import necessary package
import HostelManagement.*;

// Class representing a room in the hostel, implementing HostelComponent interface
public class Room implements HostelComponent {
    // Fields for room ID, room number, capacity, and current state
    private int roomId;
    private String roomNumber;
    private int capacity;
    private RoomState state;

    // Constructor to initialize the room with its ID, number, capacity, and initial state
    public Room(int roomId, String roomNumber, int capacity) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.state = new VacantState(); // Initial state
    }

    // Setter method to set the room's state
    public void setState(RoomState state) {
        this.state = state;
    }

    // Getter method to get the room's state
    public RoomState getState() {
        return state;
    }

    // Overridden method to display the room details
    @Override
    public void display(StringBuilder builder, String indent) {
        // Append room ID, number, capacity, and current state to the builder with the specified indentation
        builder.append(indent)
               .append("Room ID: ").append(roomId)
               .append(", Room Number: ").append(roomNumber)
               .append(", Capacity: ").append(capacity)
               .append(", State: ").append(state.getState())
               .append("\n");
    }
}
