package top.shop.backend.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class CategoryException extends AbstractException {

    public CategoryException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }
}
