package top.shop.shop1_service.exceptionhandler.exception;

import org.springframework.http.HttpStatus;

public class ProductPricingException extends AbstractException {

    public ProductPricingException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
