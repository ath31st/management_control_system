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

import java.util.*;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final RestTemplate restTemplate;

    public DiscountDto getDiscountDto(String shopUrl, String productServiceName) {
        String url = shopUrl + "/api/discount/" + productServiceName;

        return restTemplate.exchange(RequestEntity.get(url)
                .headers(TokenExtractor.headersWithTokenAuthUser())
                .build(), DiscountDto.class).getBody();
    }

    public PrivateDiscountDto getPrivateDiscountDto(String shopUrl, String productServiceName, String email) {
        String url = shopUrl + "/api/private-discount?name=" + productServiceName + "&email=" + email;

        return restTemplate.exchange(RequestEntity.get(url)
                .headers(TokenExtractor.headersWithTokenAuthUser())
                .build(), PrivateDiscountDto.class).getBody();
    }

    public CommonDiscountDto getCommonDiscountDto(String shopUrl, String productServiceName) {
        String url = shopUrl + "/api/common-discount/" + productServiceName;

        return restTemplate.exchange(RequestEntity.get(url)
                .headers(TokenExtractor.headersWithTokenAuthUser())
                .build(), CommonDiscountDto.class).getBody();
    }

    public void sendDiscountChanges(String shopUrl, DiscountDto dto) {
        String url = shopUrl + "/api/edit-discount";

        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(dto), DiscountDto.class);
    }

    public void sendPrivateDiscountChanges(String shopUrl, PrivateDiscountDto dto) {
        String url = shopUrl + "/api/edit-private-discount";

        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(dto), PrivateDiscountDto.class);
    }

    public void sendCommonDiscountChanges(String shopUrl, CommonDiscountDto dto) {
        String url = shopUrl + "/api/edit-common-discount";

        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(dto), CommonDiscountDto.class);
    }

    public DiscountWrapper getDiscounts(String shopUrl) {
        String url = shopUrl + "/api/discounts";

        return restTemplate.exchange(RequestEntity.get(url)
                .headers(TokenExtractor.headersWithTokenAuthUser())
                .build(), DiscountWrapper.class).getBody();
    }

    public void sendDiscountWrapper(DiscountWrapper discountWrapper, String shopUrl) {
        if (discountWrapper.getDiscountList().isEmpty() & discountWrapper.getPrivateDiscountList().isEmpty() &
                discountWrapper.getCommonDiscountList().isEmpty()) return;

        String url = shopUrl + "/api/new-discount";

        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(discountWrapper), DiscountWrapper.class);
    }

    public DiscountWrapper prepareDiscountWrapper(String[] productServiceNames, DiscountDto dtoFromForm) {
        DiscountWrapper discountWrapper = createEmptyWrapper();

        if (productServiceNames != null) {
            List<DiscountDto> dtoList = Arrays.stream(productServiceNames)
                    .map(s -> prepareDiscountDto(s, dtoFromForm))
                    .toList();

            discountWrapper.setDiscountList(dtoList);
        }

        return discountWrapper;
    }

    public DiscountWrapper prepareDiscountWrapper(String[] productServiceNames, CommonDiscountDto dtoFromForm) {
        DiscountWrapper discountWrapper = createEmptyWrapper();

        if (productServiceNames != null) {
            List<CommonDiscountDto> dtoList = Arrays.stream(productServiceNames)
                    .map(s -> prepareCommonDiscountDto(s, dtoFromForm))
                    .toList();

            discountWrapper.setCommonDiscountList(dtoList);
        }

        return discountWrapper;
    }

    public DiscountWrapper prepareDiscountWrapper(String[] productServiceNames, String[] customers, PrivateDiscountDto dtoFromForm) {
        DiscountWrapper discountWrapper = createEmptyWrapper();

        if (productServiceNames != null & customers != null) {
            List<PrivateDiscountDto> pDtoList = new ArrayList<>();
            Arrays.stream(customers)
                    .map(c -> Arrays.stream(productServiceNames)
                            .map(p -> preparePrivateDiscountDto(p, c, dtoFromForm))
                            .toList())
                    .forEach(pDtoList::addAll);

            discountWrapper.setPrivateDiscountList(pDtoList);
        }

        return discountWrapper;
    }

    public List<String> getCustomersEmail(String shopUrl) {
        String url = shopUrl + "/api/customers";

        return List.of(Objects.requireNonNull(restTemplate.exchange(RequestEntity.get(url)
                .headers(TokenExtractor.headersWithTokenAuthUser())
                .build(), String[].class).getBody()));
    }

    private DiscountDto prepareDiscountDto(String productServiceName, DiscountDto dtoFromForm) {
        DiscountDto dto = new DiscountDto();
        dto.setProductServiceName(productServiceName);
        dto.setStartingDate(dtoFromForm.getStartingDate());
        dto.setEndingDate(dtoFromForm.getEndingDate());
        dto.setPercentageDiscount(dtoFromForm.getPercentageDiscount());
        dto.setActive(dtoFromForm.isActive());
        return dto;
    }

    private PrivateDiscountDto preparePrivateDiscountDto(String productServiceName, String customerEmail, PrivateDiscountDto dtoFromForm) {
        PrivateDiscountDto dto = new PrivateDiscountDto();
        dto.setProductServiceName(productServiceName);
        dto.setStartingDate(dtoFromForm.getStartingDate());
        dto.setEndingDate(dtoFromForm.getEndingDate());
        dto.setPercentageDiscount(dtoFromForm.getPercentageDiscount());
        dto.setActive(dtoFromForm.isActive());

        dto.setStacking(dtoFromForm.isStacking());
        dto.setPromoCode(dtoFromForm.getPromoCode());
        dto.setCustomerEmail(customerEmail);
        return dto;
    }

    private CommonDiscountDto prepareCommonDiscountDto(String productServiceName, CommonDiscountDto dtoFromForm) {
        CommonDiscountDto dto = new CommonDiscountDto();
        dto.setProductServiceName(productServiceName);
        dto.setStartingDate(dtoFromForm.getStartingDate());
        dto.setEndingDate(dtoFromForm.getEndingDate());
        dto.setPercentageDiscount(dtoFromForm.getPercentageDiscount());
        dto.setActive(dtoFromForm.isActive());

        dto.setStacking(dtoFromForm.isStacking());
        dto.setPromoCode(dtoFromForm.getPromoCode());
        dto.setNumberOfAvailable(dtoFromForm.getNumberOfAvailable());
        return dto;
    }

    private DiscountWrapper createEmptyWrapper() {
        DiscountWrapper wrapper = new DiscountWrapper();
        wrapper.setDiscountList(Collections.emptyList());
        wrapper.setPrivateDiscountList(Collections.emptyList());
        wrapper.setCommonDiscountList(Collections.emptyList());
        return wrapper;
    }


}
