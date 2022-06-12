package ensa.ebanking.accountservice.Exceptions;

public class BankAccountNotFoundException extends RuntimeException implements PaymentException {
    public BankAccountNotFoundException(String message) {
        super(message);
    }

    @Override
    public int errorCode() {
        return 424;
    }
}
