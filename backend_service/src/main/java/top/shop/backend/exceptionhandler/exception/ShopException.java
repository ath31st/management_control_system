package top.shop.backend.exceptionhandler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ShopException extends AbstractException{

    public ShopException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
