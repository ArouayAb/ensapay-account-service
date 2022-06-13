package ensa.ebanking.accountservice.Exceptions;

public class WrongCreancierCategoryException extends RuntimeException implements PaymentException{

    public WrongCreancierCategoryException(String message) {
        super(message);
    }

    @Override
    public int errorCode() {
        return 400;
    }
}
