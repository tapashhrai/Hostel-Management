// Abstract factory class for creating User objects
public abstract class UserFactory {
     /**
     * Abstract method to create a User object.
     * Subclasses of UserFactory must provide an implementation for this method.
     *
     * @param username The username of the user.
     * @param enrollmentNumber The enrollment number of the user.
     * @param email The email address of the user.
     * @param hashedPassword The hashed password of the user.
     * @param gender The gender of the user.
     * @param year The year associated with the user (e.g., enrollment year).
     * @return A User object.
     */
    public abstract User createUser(String username, String enrollmentNumber, String email, String hashedPassword, String gender, int year);
}

