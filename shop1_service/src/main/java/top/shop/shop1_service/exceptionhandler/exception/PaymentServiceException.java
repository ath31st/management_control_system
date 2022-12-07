package top.shop.shop1_service.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class PaymentServiceException extends AbstractException {

    public PaymentServiceException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
