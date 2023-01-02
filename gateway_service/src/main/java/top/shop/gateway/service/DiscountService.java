package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.discount.DiscountDto;
import top.shop.gateway.util.TokenExtractor;
import top.shop.gateway.util.wrapper.DiscountWrapper;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final RestTemplate restTemplate;

    public DiscountWrapper getDiscounts(String shopUrl) {
        String url = shopUrl + "/api/discounts";

        return restTemplate.exchange(RequestEntity.get(url)
                .headers(TokenExtractor.headersWithTokenAuthUser())
                .build(), DiscountWrapper.class).getBody();
    }

    public void sendDiscountWrapper(DiscountWrapper discountWrapper, String shopUrl) {
        String url = shopUrl + "/api/new-discount";

        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(discountWrapper), DiscountWrapper.class);
    }

    public DiscountWrapper prepareDiscountWrapper(String[] productServiceNames, DiscountDto dtoFromForm, String shopServiceName) {
        DiscountWrapper discountWrapper = new DiscountWrapper();

        List<DiscountDto> dtoList = Arrays.stream(productServiceNames)
                .map(s -> {
                    DiscountDto discountDto = new DiscountDto();
                    discountDto.setProductServiceName(s);
                    discountDto.setShopServiceName(shopServiceName);
                    discountDto.setStartingDate(dtoFromForm.getStartingDate());
                    discountDto.setEndingDate(dtoFromForm.getEndingDate());
                    discountDto.setPercentageDiscount(dtoFromForm.getPercentageDiscount());
                    discountDto.setActive(dtoFromForm.isActive());
                    return discountDto;
                })
                .toList();

        discountWrapper.setDiscountList(dtoList);
        return discountWrapper;
    }
}
