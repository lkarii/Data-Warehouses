package data.warehouses;

public class BadInputException extends RuntimeException {
    public BadInputException(String excMessage) {
        super(excMessage);
    }
}
