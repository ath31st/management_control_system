package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.backend.repository.discount.CommonDiscountRepository;
import top.shop.backend.repository.discount.DiscountRepository;
import top.shop.backend.repository.discount.PrivateDiscountRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final PrivateDiscountRepository privateDiscountRepository;
    private final CommonDiscountRepository commonDiscountRepository;
}
