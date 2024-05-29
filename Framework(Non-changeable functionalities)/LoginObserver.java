// Interface for observing login events
public interface LoginObserver {
    void onLoginSuccess(String userType);
    /**
     * Method to handle successful login events.
     * Classes implementing this interface must provide an implementation for this method.
     *
     * @param userType The type of user that has successfully logged in (e.g., admin, student).
     */
      
    void onLoginFailure();
    /**
     * Method to handle login failure events.
     * Classes implementing this interface must provide an implementation for this method.
     */
}