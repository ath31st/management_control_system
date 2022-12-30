package top.shop.shop1_service.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class CustomerServiceException extends AbstractException {

    public CustomerServiceException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
