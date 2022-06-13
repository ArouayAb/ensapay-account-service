package ensa.ebanking.accountservice.Exceptions;

public class NotEnoughBalanceException extends RuntimeException implements PaymentException {
    public NotEnoughBalanceException(String message) {
        super(message);
    }

    @Override
    public int errorCode() {
        return 400;
    }
}
