// Import necessary package
import HostelManagement.*;

// Proxy class that handles payment processing and delegates to RealPayment
public class PaymentProxy implements Payment {
    // Reference to the real payment object
    private RealPayment realPayment;

    // Overridden method to make a payment
    @Override
    public void makePayment(String studentEmail) {
        // Initialize realPayment if it is not already initialized
        if (realPayment == null) {
            realPayment = new RealPayment();
        }
        // Delegate the payment request to the real payment object
        realPayment.makePayment(studentEmail);
    }
}
