package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.discount.CommonDiscountDto;
import top.shop.backend.dto.discount.DiscountDto;
import top.shop.backend.dto.discount.PrivateDiscountDto;
import top.shop.backend.entity.discount.CommonDiscount;
import top.shop.backend.entity.discount.Discount;
import top.shop.backend.entity.discount.PrivateDiscount;
import top.shop.backend.repository.discount.CommonDiscountRepository;
import top.shop.backend.repository.discount.DiscountRepository;
import top.shop.backend.repository.discount.PrivateDiscountRepository;
import top.shop.backend.util.wrapper.DiscountWrapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final PrivateDiscountRepository privateDiscountRepository;
    private final CommonDiscountRepository commonDiscountRepository;
    private final ProductService productService;
    private final ShopService shopService;

    public DiscountWrapper getDiscountWrapper() {
        DiscountWrapper discountWrapper = new DiscountWrapper();
        discountWrapper.setDiscountList(getDiscountDtoList());
        discountWrapper.setPrivateDiscountList(getPrivateDiscountDtoList());
        discountWrapper.setCommonDiscountList(getCommonDiscountDtoList());
        return discountWrapper;
    }

    public List<DiscountDto> getDiscountDtoList() {
        return discountRepository.findAll()
                .stream()
                .map(this::discountObjectMapper)
                .toList();
    }

    public List<PrivateDiscountDto> getPrivateDiscountDtoList() {
        return privateDiscountRepository.findAll()
                .stream()
                .map(this::privateDiscountObjectMapper)
                .toList();
    }

    public List<CommonDiscountDto> getCommonDiscountDtoList() {
        return commonDiscountRepository.findAll()
                .stream()
                .map(this::commonDiscountDtoObjectMapper)
                .toList();
    }

    private DiscountDto discountObjectMapper(Discount discount) {
        DiscountDto dto = new DiscountDto();
        dto.setProductServiceName(discount.getProduct().getServiceName());
        dto.setProductName(discount.getProduct().getName());
        dto.setShopServiceName(discount.getShop().getServiceName());
        dto.setShopName(discount.getShop().getName());
        dto.setStartingDate(discount.getStartingDate());
        dto.setEndingDate(discount.getEndingDate());
        dto.setPercentageDiscount(discount.getPercentageDiscount());
        dto.setActive(discount.isActive());
        return dto;
    }

    private PrivateDiscountDto privateDiscountObjectMapper(PrivateDiscount privateDiscount) {
        PrivateDiscountDto dto = (PrivateDiscountDto) discountObjectMapper(privateDiscount);
        dto.setPromoCode(privateDiscount.getPromoCode());
        dto.setStacking(privateDiscount.isStacking());
        return dto;
    }

    private CommonDiscountDto commonDiscountDtoObjectMapper(CommonDiscount commonDiscount) {
        CommonDiscountDto dto = (CommonDiscountDto) privateDiscountObjectMapper(commonDiscount);
        dto.setNumberOfAvailable(commonDiscount.getNumberOfAvailable());
        return dto;
    }

    public void saveDiscount(DiscountDto discountDto) {
       // TODO relocate this service to shop
    }
}
