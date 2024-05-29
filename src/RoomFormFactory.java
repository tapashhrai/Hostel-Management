// Import necessary package
import javax.swing.JFrame;
import HostelManagement.*;

// Implementation of the FormFactory interface to create a form for adding rooms
public class RoomFormFactory implements FormFactory {
    // Overridden method to create and return a new AddRoomForm
    @Override
    public JFrame createForm() {
        return new AddRoomForm();
    }
}