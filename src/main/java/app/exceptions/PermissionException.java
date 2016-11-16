package app.exceptions;

/**
 * Created by Admin on 12.11.2016.
 */
public class PermissionException extends Exception {
    public PermissionException() {
        super("Access denied!");
    }
}
