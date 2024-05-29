// Import necessary package
import HostelManagement.*;

// Implementation of the RoomState interface representing an occupied state
public class OccupiedState implements RoomState {

    // Overridden method to handle the state transition for a room
    @Override
    public void handle(Room room) {
        // Set the room's state to VacantState
        room.setState(new VacantState());
    }

    // Overridden method to get the current state as a string
    @Override
    public String getState() {
        return "occupied";
    }
}
