package top.shop.backend.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class ProductException extends AbstractException {

    public ProductException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
