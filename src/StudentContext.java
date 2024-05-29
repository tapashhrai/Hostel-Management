// Import necessary package
import HostelManagement.*;

// Class representing the context of a student, maintaining a reference to a state
public class StudentContext {
    // Fields for the current state and enrollment number of the student
    private StudentState state;
    private String enrollmentNumber;

    // Constructor to initialize the student context with an enrollment number
    public StudentContext(String enrollmentNumber) {
        this.enrollmentNumber = enrollmentNumber;
        // Default state is active
        this.state = new ActiveState();
    }

    // Setter method to set the student's state
    public void setState(StudentState state) {
        this.state = state;
    }

    // Getter method to get the student's state
    public StudentState getState() {
        return state;
    }

    // Getter method to get the student's enrollment number
    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }

    // Method to apply the current state's behavior
    public void applyState() {
        state.handleState(this);
    }
}
