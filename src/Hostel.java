// Import necessary packages
import java.util.List;
import HostelManagement.*;

// Implementation of the HostelComponent interface representing a hostel
public class Hostel implements HostelComponent {
    // Fields for hostel ID, hostel name, and a list of rooms
    private int hostelId;
    private String hostelName;
    private List<HostelComponent> rooms;

    // Constructor to initialize the hostel with its ID, name, and rooms
    public Hostel(int hostelId, String hostelName, List<HostelComponent> rooms) {
        this.hostelId = hostelId;
        this.hostelName = hostelName;
        this.rooms = rooms;
    }

    // Overridden method to display the hostel details and its rooms
    @Override
    public void display(StringBuilder builder, String indent) {
        // Append hostel ID and name to the builder with the specified indentation
        builder.append(indent)
               .append("Hostel ID: ").append(hostelId)
               .append(", Hostel Name: ").append(hostelName)
               .append("\n");
        // Loop through each room and display its details with increased indentation
        for (HostelComponent room : rooms) {
            room.display(builder, indent + "  ");
        }
    }
}
