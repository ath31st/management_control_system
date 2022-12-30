package top.shop.shop1_service.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.shop.shop1_service.dto.CustomerDto;
import top.shop.shop1_service.service.CustomerService;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CustomerService customerService;

    @Override
    public void run(String... args) {
        createCustomers();
    }

    private void createCustomers() {
        CustomerDto dto1 = new CustomerDto();
        dto1.setBalance(BigDecimal.valueOf(106770));
        dto1.setUsername("first_customer");

        CustomerDto dto2 = new CustomerDto();
        dto2.setBalance(BigDecimal.valueOf(550342));
        dto2.setUsername("second_customer");

        customerService.saveCustomer(dto1);
        customerService.saveCustomer(dto2);
    }

}