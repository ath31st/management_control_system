package top.shop.shop1_service.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class ProductPriceException extends AbstractException {

    public ProductPriceException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
