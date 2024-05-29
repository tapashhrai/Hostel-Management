// Import necessary packages
import javax.swing.JFrame;
import HostelManagement.*;

// Implementation of the FormFactory interface to create a form for adding hostels
public class HostelFormFactory implements FormFactory {
    // Overridden method to create and return a new AddHostelForm
    @Override
    public JFrame createForm() {
        return new AddHostelForm();
    }
}
