package top.shop.shop1_service.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class ProductServiceException extends AbstractException {

    public ProductServiceException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
