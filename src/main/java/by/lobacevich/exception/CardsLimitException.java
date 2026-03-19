package by.lobacevich.exception;

public class CardsLimitException extends RuntimeException {

    public CardsLimitException(String message) {
        super(message);
    }
}
