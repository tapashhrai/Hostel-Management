// Import necessary package
import HostelManagement.*;

// Class representing the context in the strategy pattern, maintaining a reference to a strategy
public class RoomAllocator {
    // Field to hold the current strategy
    private RoomAllocationStrategy strategy;

    // Setter method to set the strategy
    public void setStrategy(RoomAllocationStrategy strategy) {
        this.strategy = strategy;
    }

    // Method to allocate rooms using the current strategy
    public void allocate() {
        // Check if a strategy is set
        if (strategy != null) {
            // Call the strategy's method to allocate rooms
            strategy.allocateRooms();
        } else {
            // Throw an exception if no strategy is set
            throw new IllegalStateException("Strategy not set.");
        }
    }
}
