package top.shop.shop1_service.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class CatalogueException extends AbstractException {

    public CatalogueException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
