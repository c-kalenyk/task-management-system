package mate.academy.taskmanagementsystem.exception;

public class UsernameOrEmailExistsException extends RuntimeException {
    public UsernameOrEmailExistsException(String message) {
        super(message);
    }
}
