package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.CategoryDto;
import top.shop.gateway.dto.ShopDto;
import top.shop.gateway.util.TokenExtractor;

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
        ShopDto[] shops = restTemplate.exchange(RequestEntity.get(url)
                        .headers(TokenExtractor.headersWithTokenAuthUser())
                        .build(), ShopDto[].class).getBody();

        if (shops == null) return new ArrayList<>();

        return new ArrayList<>(List.of(shops));
    }

    public ShopDto getShop(String shopServiceName) {
        String url = backendUrl + "/api/shops/" + shopServiceName;
        return restTemplate.exchange(RequestEntity.get(url)
                        .headers(TokenExtractor.headersWithTokenAuthUser())
                        .build(), ShopDto.class).getBody();
    }

    public double getTotalBalance(List<ShopDto> shops) {
        return shops.stream()
                .mapToDouble(ShopDto::getBalance)
                .sum();
    }

    public void createShop(ShopDto shopDto) {
        String url = backendUrl + "/api/new-shop";
        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(shopDto), ShopDto.class);
    }

    public void sendShopChanges(ShopDto shopDto) {
        String url = backendUrl + "/api/edit-shop";
        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(shopDto), ShopDto.class);
    }

}
