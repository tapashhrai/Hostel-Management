// Import the necessary package for jar file
import HostelManagement.*;

// Implementation of the StudentState interface representing the active state of a student
public class ActiveState implements StudentState {
    @Override // Overridden method to handle the current state of the student
    public void handleState(StudentContext context) {
        System.out.println("Student is currently active.");// Print a message indicating the student is currently active
    }
}


