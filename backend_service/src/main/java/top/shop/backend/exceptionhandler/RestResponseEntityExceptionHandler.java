package top.shop.backend.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import top.shop.backend.exceptionhandler.exception.*;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ShopException.class)
    protected ResponseEntity<Response> handleException(ShopException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(ProductException.class)
    protected ResponseEntity<Response> handleException(ProductException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(OrderServiceException.class)
    protected ResponseEntity<Response> handleException(OrderServiceException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(CatalogueException.class)
    protected ResponseEntity<Response> handleException(CatalogueException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(CategoryException.class)
    protected ResponseEntity<Response> handleException(CategoryException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    private Response buildResponse(AbstractException e) {
        return Response.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(e.getMessage())
                .status(e.getStatus())
                .build();
    }
}
