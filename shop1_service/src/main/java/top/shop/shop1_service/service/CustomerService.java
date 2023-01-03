package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CustomerDto;
import top.shop.shop1_service.entity.Customer;
import top.shop.shop1_service.repository.CustomerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer saveCustomer(CustomerDto dto) {
        if (customerRepository.existsByUsername(dto.getUsername())) {
            return getCustomer(dto.getUsername());
        } else {
            Customer customer = new Customer();
            customer.setBalance(dto.getBalance());
            customer.setUsername(dto.getUsername());

            return customerRepository.save(customer);
        }
    }

    public Customer getCustomer(String username) {
        return customerRepository.findByUsername(username);
    }

    public String[] getCustomersUsername() {
        return customerRepository.customersUsername();
    }

    public boolean isExistsCustomer(String username) {
        return customerRepository.existsByUsername(username);
    }
}
