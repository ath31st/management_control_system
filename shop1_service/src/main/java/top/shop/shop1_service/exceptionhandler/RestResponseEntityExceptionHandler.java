package top.shop.shop1_service.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import top.shop.shop1_service.exceptionhandler.exception.*;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductPricingException.class)
    protected ResponseEntity<Response> handleCustomException(ProductPricingException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(OrderServiceException.class)
    protected ResponseEntity<Response> handleCustomException(OrderServiceException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(CatalogueException.class)
    protected ResponseEntity<Response> handleCustomException(CatalogueException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(PaymentServiceException.class)
    protected ResponseEntity<Response> handleCustomException(PaymentServiceException e) {
        return new ResponseEntity<>(buildResponse(e), e.getStatus());
    }

    @ExceptionHandler(DeliveryServiceException.class)
    protected ResponseEntity<Response> handleCustomException(DeliveryServiceException e) {
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
