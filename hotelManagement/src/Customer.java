import java.util.regex.Pattern;

public class Customer {

    private static final String EMAIL_REGEX_PATTERN = "^(.+)@(.+).(.+)$";

    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(final String firstName, final String lastName, final String email) {
        this.isValidEmail(email);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Validates the email.
     * @param email that needs the validation
     */
    private void isValidEmail(final String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX_PATTERN);

        if(!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public String getEmail() {
        return this.email;
    }

    /**
     * This method gets all user information
     * @return user's information
     */
    public String toString() {
        return "First Name: " + this.firstName
                + " Last Name: " + this.lastName
                + " Email: " + this.email;
    }
}
