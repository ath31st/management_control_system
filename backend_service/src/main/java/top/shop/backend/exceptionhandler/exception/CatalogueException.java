package top.shop.backend.exceptionhandler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CatalogueException extends AbstractException{

    public CatalogueException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }

}
