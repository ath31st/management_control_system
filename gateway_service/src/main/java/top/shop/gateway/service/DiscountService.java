package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.discount.CommonDiscountDto;
import top.shop.gateway.dto.discount.DiscountDto;
import top.shop.gateway.dto.discount.PrivateDiscountDto;
import top.shop.gateway.util.TokenExtractor;
import top.shop.gateway.util.wrapper.DiscountWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
                .map(s -> prepareDiscountDto(s, dtoFromForm, shopServiceName))
                .toList();

        discountWrapper.setDiscountList(dtoList);
        return discountWrapper;
    }

    public DiscountWrapper prepareDiscountWrapper(String[] productServiceNames, CommonDiscountDto dtoFromForm, String shopServiceName) {
        DiscountWrapper discountWrapper = new DiscountWrapper();

        List<CommonDiscountDto> dtoList = Arrays.stream(productServiceNames)
                .map(s -> prepareCommonDiscountDto(s, dtoFromForm, shopServiceName))
                .toList();

        discountWrapper.setCommonDiscountList(dtoList);
        return discountWrapper;
    }

    public DiscountWrapper prepareDiscountWrapper(String[] productServiceNames, String[] customers, PrivateDiscountDto dtoFromForm, String shopServiceName) {
        DiscountWrapper discountWrapper = new DiscountWrapper();

        List<PrivateDiscountDto> pDtoList = new ArrayList<>();
        Arrays.stream(customers)
                .map(c -> Arrays.stream(productServiceNames)
                        .map(p -> preparePrivateDiscountDto(p, c, dtoFromForm, shopServiceName))
                        .toList())
                .forEach(pDtoList::addAll);

        discountWrapper.setPrivateDiscountList(pDtoList);
        return discountWrapper;
    }

    public List<String> getCustomersUsername(String shopUrl) {
        String url = shopUrl + "/api/customers";

        return List.of(Objects.requireNonNull(restTemplate.exchange(RequestEntity.get(url)
                .headers(TokenExtractor.headersWithTokenAuthUser())
                .build(), String[].class).getBody()));
    }

    private DiscountDto prepareDiscountDto(String productServiceName, DiscountDto dtoFromForm, String shopServiceName) {
        DiscountDto dto = new DiscountDto();
        dto.setProductServiceName(productServiceName);
        dto.setShopServiceName(shopServiceName);
        dto.setStartingDate(dtoFromForm.getStartingDate());
        dto.setEndingDate(dtoFromForm.getEndingDate());
        dto.setPercentageDiscount(dtoFromForm.getPercentageDiscount());
        dto.setActive(dtoFromForm.isActive());
        return dto;
    }

    private PrivateDiscountDto preparePrivateDiscountDto(String productServiceName, String customerUsername, PrivateDiscountDto dtoFromForm, String shopServiceName) {
        PrivateDiscountDto dto = new PrivateDiscountDto();
        dto.setProductServiceName(productServiceName);
        dto.setShopServiceName(shopServiceName);
        dto.setStartingDate(dtoFromForm.getStartingDate());
        dto.setEndingDate(dtoFromForm.getEndingDate());
        dto.setPercentageDiscount(dtoFromForm.getPercentageDiscount());
        dto.setActive(dtoFromForm.isActive());

        dto.setStacking(dtoFromForm.isStacking());
        dto.setPromoCode(dtoFromForm.getPromoCode());
        dto.setCustomerUsername(customerUsername);
        return dto;
    }

    private CommonDiscountDto prepareCommonDiscountDto(String productServiceName, CommonDiscountDto dtoFromForm, String shopServiceName) {
        CommonDiscountDto dto = new CommonDiscountDto();
        dto.setProductServiceName(productServiceName);
        dto.setShopServiceName(shopServiceName);
        dto.setStartingDate(dtoFromForm.getStartingDate());
        dto.setEndingDate(dtoFromForm.getEndingDate());
        dto.setPercentageDiscount(dtoFromForm.getPercentageDiscount());
        dto.setActive(dtoFromForm.isActive());

        dto.setStacking(dtoFromForm.isStacking());
        dto.setPromoCode(dtoFromForm.getPromoCode());
        dto.setNumberOfAvailable(dtoFromForm.getNumberOfAvailable());
        return dto;
    }
}
