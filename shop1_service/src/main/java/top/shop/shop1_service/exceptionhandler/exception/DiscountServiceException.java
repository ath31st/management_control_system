package top.shop.shop1_service.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class DiscountServiceException extends AbstractException {

    public DiscountServiceException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
