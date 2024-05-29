// Interface for processing payments
public interface Payment {

    /**
     * Method to make a payment for a student.
     * Classes implementing this interface must provide an implementation for this method.
     *
     * @param studentEmail The email address of the student for whom the payment is being made.
     */
    void makePayment(String studentEmail);
}
