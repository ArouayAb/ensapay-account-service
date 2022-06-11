package ensa.ebanking.accountservice.Exceptions;

public class CreanceAlreadyPaidException extends RuntimeException implements PaymentException {
    public CreanceAlreadyPaidException(String message) {
        super(message);
    }

    @Override
    public int errorCode() {
        return 409;
    }
}
