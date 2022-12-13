package top.shop.shop1_service.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class DeliveryServiceException extends AbstractException {

    public DeliveryServiceException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
