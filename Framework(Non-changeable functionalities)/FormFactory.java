import javax.swing.JFrame;
// Interface for a factory that creates JFrame objects
public interface FormFactory {
      /**
     * Method to create a JFrame form.
     * Classes implementing this interface must provide an implementation for this method.
     *
     * @return A new instance of a JFrame.
     */
    JFrame createForm();
}
