// Import necessary package
import HostelManagement.*;

// Implementation of the RoomState interface representing a vacant state of a room
public class VacantState implements RoomState {

    // Overridden method to handle the state transition for a room
    @Override
    public void handle(Room room) {
        // Set the room's state to OccupiedState
        room.setState(new OccupiedState());
    }

    // Overridden method to get the current state as a string
    @Override
    public String getState() {
        return "vacant";
    }
}
