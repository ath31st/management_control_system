package top.shop.shop1_service.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import top.shop.shop1_service.exceptionhandler.exception.AbstractException;
import top.shop.shop1_service.exceptionhandler.exception.OrderServiceException;
import top.shop.shop1_service.exceptionhandler.exception.ProductPricingException;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductPricingException.class)
    protected ResponseEntity<Response> handlePayloadException(ProductPricingException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(OrderServiceException.class)
    protected ResponseEntity<Response> handlePayloadException(OrderServiceException e) {
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
