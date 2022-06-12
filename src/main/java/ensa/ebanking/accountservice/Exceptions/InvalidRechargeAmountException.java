package ensa.ebanking.accountservice.Exceptions;

public class InvalidRechargeAmountException extends RuntimeException implements PaymentException{

    public InvalidRechargeAmountException(String message) {
        super(message);
    }

    @Override
    public int errorCode() {
        return 400;
    }
}
