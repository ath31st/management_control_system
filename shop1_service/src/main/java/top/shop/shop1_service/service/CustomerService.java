package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CustomerDto;
import top.shop.shop1_service.entity.Customer;
import top.shop.shop1_service.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer saveCustomer(CustomerDto dto) {
        if (customerRepository.existsByEmail(dto.getEmail())) {
            return getCustomer(dto.getEmail());
        } else {
            Customer customer = new Customer();
            customer.setEmail(dto.getEmail());
            customer.setBalance(dto.getBalance());
            customer.setUsername(dto.getUsername());

            return customerRepository.save(customer);
        }
    }

    public Customer getCustomer(String email) {
        return customerRepository.findByEmail(email);
    }

    public String[] getCustomersEmail() {
        return customerRepository.customersEmail();
    }

    public boolean isExistsCustomer(String email) {
        return customerRepository.existsByEmail(email);
    }
}
