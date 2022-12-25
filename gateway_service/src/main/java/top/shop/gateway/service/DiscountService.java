package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.discount.CommonDiscountDto;
import top.shop.gateway.dto.discount.DiscountDto;
import top.shop.gateway.dto.discount.PrivateDiscountDto;
import top.shop.gateway.util.TokenExtractor;
import top.shop.gateway.util.wrapper.DiscountWrapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {
    @Value("${backend.url}")
    private String backendUrl;
    private final RestTemplate restTemplate;

    public DiscountWrapper getDiscounts() {
        String url = backendUrl + "/api/discounts";

        return restTemplate.exchange(RequestEntity.get(url)
                .headers(TokenExtractor.headersWithTokenAuthUser())
                .build(), DiscountWrapper.class).getBody();
    }
}
