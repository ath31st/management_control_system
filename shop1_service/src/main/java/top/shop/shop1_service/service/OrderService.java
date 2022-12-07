package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.dto.PaymentDto;
import top.shop.shop1_service.exceptionhandler.exception.OrderServiceException;
import top.shop.shop1_service.config.kafkaconfig.OrderProducer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${shop.service-name}")
    private String serviceName;
    private final CatalogueService catalogueService;
    private final OrderProducer orderProducer;
    private final ProductPricingService productPricingService;

    @Transactional
    public PaymentDto createOrder(OrderDto orderDto) {
        checkAvailableProductForSale(orderDto.getProductName());
        checkAvailableAmountProductForSale(orderDto.getProductName(), orderDto.getAmount());

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentDate(LocalDateTime.now());
        paymentDto.setPaymentUuid(UUID.randomUUID().toString());
        paymentDto.setExecuted(false);
        paymentDto.setMinutesBeforeExpiration(5);
        paymentDto.setTotalPrice(BigDecimal.valueOf(orderDto.getAmount())
                .multiply(BigDecimal.valueOf(orderDto.getPrice())));

        orderDto.setPaymentDto(paymentDto);
        orderDto.setShopServiceName(serviceName);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setPrice(productPricingService.getProductPricing(orderDto.getProductName()).getPrice());

        try {
            orderProducer.sendMessage(orderDto);
        } catch (JsonProcessingException e) {
            throw new OrderServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
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

}
