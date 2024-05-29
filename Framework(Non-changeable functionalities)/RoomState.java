// Interface for defining the state of a room and handling state transitions
public interface RoomState {

    void handle(Room room);
    // Method to handle the current state of the room.\
    //@param room The Room object whose state is being handled.

    String getState();
    //Method to get the current state of the room as a string.
    //@return The current state of the room as a string.
}
