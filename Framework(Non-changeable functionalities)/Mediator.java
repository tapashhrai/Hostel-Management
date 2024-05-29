// Interface for mediating feedback communication
public interface Mediator {
     /**
     * Method to send feedback.
     * @param email The email address to which the feedback will be sent.
     * @param feedback The feedback message to be sent.
     */
    void sendFeedback(String email, String feedback);
}

