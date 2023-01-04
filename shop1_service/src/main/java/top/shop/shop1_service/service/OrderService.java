package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.shop.shop1_service.config.kafkaconfig.OrderProducer;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.dto.payment.PaymentDto;
import top.shop.shop1_service.entity.Customer;
import top.shop.shop1_service.exceptionhandler.exception.CustomerServiceException;
import top.shop.shop1_service.exceptionhandler.exception.OrderServiceException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${shop.service-name}")
    private String serviceName;
    private final CatalogueService catalogueService;
    private final OrderProducer orderProducer;
    private final CustomerService customerService;
    private final PaymentService paymentService;

    @Transactional
    public PaymentDto createOrder(OrderDto orderDto) {
        checkAvailableProductForSale(orderDto.getProductName());
        checkAvailableAmountProductForSale(orderDto.getProductName(), orderDto.getAmount());
        checkExistsCustomer(orderDto.getCustomerEmail());

        Customer customer = customerService.getCustomer(orderDto.getCustomerEmail());

        PaymentDto paymentDto = paymentService.getPaymentDtoFromPayment(
                paymentService.createPayment(customer, orderDto.getProductName(), orderDto.getAmount()));

        orderDto.setPaymentDto(paymentDto);
        orderDto.setShopServiceName(serviceName);
        orderDto.setOrderDate(LocalDateTime.now());

        try {
            //orderProducer.sendMessage(orderDto);
            orderProducer.sendMessageWithCallback(orderDto);
        } catch (JsonProcessingException | OrderServiceException e) {
            paymentService.cancelPayment(paymentDto.getPaymentUuid());
            throw new OrderServiceException(HttpStatus.BAD_REQUEST, "Payment and order was closed.");
        }
        return paymentDto;
    }

    private void checkAvailableProductForSale(String productServiceName) {
        if (!catalogueService.getProductServiceNameList().contains(productServiceName))
            throw new OrderServiceException(HttpStatus.BAD_REQUEST, "The product is absent in the catalogue");
    }

    private void checkAvailableAmountProductForSale(String productServiceName, int orderAmount) {
        if (!((catalogueService.getAmountProductFromCatalogue(productServiceName) - orderAmount) >= 0))
            throw new OrderServiceException(HttpStatus.BAD_REQUEST, "The product is not enough for the order");
    }

    private void checkExistsCustomer(String email) {
        if (!customerService.isExistsCustomer(email))
            throw new CustomerServiceException(HttpStatus.NOT_FOUND, "The customer with email: " + email + " not found.");
    }

}
