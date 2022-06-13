package ensa.ebanking.accountservice.Exceptions;

public class BankAccountAlreadyExistException extends RuntimeException implements PaymentException{

    public BankAccountAlreadyExistException(String message) {
        super(message);
    }

    @Override
    public int errorCode() {
        return 409;
    }
}
