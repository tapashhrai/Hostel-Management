// Abstract class representing a generic User
public abstract class User {
    // Protected attributes to store user details
    protected String username;
    protected String enrollmentNumber;
    protected String email;
    protected String hashedPassword;

    //Constructor to initialize a User object.
    public User(String username, String enrollmentNumber, String email, String hashedPassword) {
        this.username = username;
        this.enrollmentNumber = enrollmentNumber;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }
}

