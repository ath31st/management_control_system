package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.ShopDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {
    @Value("${backend.url}")
    private String backendUrl;
    private final RestTemplate restTemplate;

    public List<ShopDto> getShops() {
        String url = backendUrl + "/api/shops";
        ShopDto[] shops = restTemplate.getForObject(url, ShopDto[].class);

        if (shops == null) return new ArrayList<>();

        return new ArrayList<>(List.of(shops));
    }

    public double getTotalBalance(List<ShopDto> shops) {
        return shops.stream()
                .mapToDouble(ShopDto::getBalance)
                .sum();
    }

    public void createShop(ShopDto shopDto) {
        String url = backendUrl + "/api/new-shop";
        restTemplate.postForObject(url, shopDto, ShopDto.class);
    }
}
