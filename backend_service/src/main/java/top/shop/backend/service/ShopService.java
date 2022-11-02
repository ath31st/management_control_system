package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.shop.backend.repository.ShopRepository;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
}
