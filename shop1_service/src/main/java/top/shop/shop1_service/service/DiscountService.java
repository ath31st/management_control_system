package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.discount.CommonDiscountDto;
import top.shop.shop1_service.dto.discount.DiscountDto;
import top.shop.shop1_service.dto.discount.PrivateDiscountDto;
import top.shop.shop1_service.entity.discount.CommonDiscount;
import top.shop.shop1_service.entity.discount.Discount;
import top.shop.shop1_service.entity.discount.PrivateDiscount;
import top.shop.shop1_service.exceptionhandler.exception.DiscountServiceException;
import top.shop.shop1_service.repository.discount.CommonDiscountRepository;
import top.shop.shop1_service.repository.discount.DiscountRepository;
import top.shop.shop1_service.repository.discount.PrivateDiscountRepository;
import top.shop.shop1_service.util.wrapper.DiscountWrapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final PrivateDiscountRepository privateDiscountRepository;
    private final CommonDiscountRepository commonDiscountRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    public DiscountWrapper getDiscountWrapper() {
        DiscountWrapper discountWrapper = new DiscountWrapper();
        discountWrapper.setDiscountList(getDiscountDtoList());
        discountWrapper.setPrivateDiscountList(getPrivateDiscountDtoList());
        discountWrapper.setCommonDiscountList(getCommonDiscountDtoList());
        return discountWrapper;
    }

    private List<DiscountDto> getDiscountDtoList() {
        return discountRepository.findAllDiscount()
                .stream()
                .map(this::discountToDtoConverter)
                .toList();
    }

    public List<DiscountDto> getActiveDiscountDtoList() {
        return discountRepository.findAllActiveDiscount()
                .stream()
                .map(this::discountToDtoConverter)
                .toList();
    }

    private List<PrivateDiscountDto> getPrivateDiscountDtoList() {
        return privateDiscountRepository.findAll()
                .stream()
                .map(this::privateDiscountToDtoConverter)
                .toList();
    }

    private List<CommonDiscountDto> getCommonDiscountDtoList() {
        return commonDiscountRepository.findAll()
                .stream()
                .map(this::commonDiscountToDtoConverter)
                .toList();
    }

    private DiscountDto discountToDtoConverter(Discount discount) {
        DiscountDto dto = new DiscountDto();
        dto.setProductServiceName(discount.getProduct().getServiceName());
        dto.setProductName(discount.getProduct().getName());
        dto.setStartingDate(discount.getStartingDate());
        dto.setEndingDate(discount.getEndingDate());
        dto.setPercentageDiscount(discount.getPercentageDiscount());
        dto.setActive(discount.isActive());

        return dto;
    }

    private PrivateDiscountDto privateDiscountToDtoConverter(PrivateDiscount privateDiscount) {
        PrivateDiscountDto dto = new PrivateDiscountDto();
        dto.setProductServiceName(privateDiscount.getProduct().getServiceName());
        dto.setProductName(privateDiscount.getProduct().getName());
        dto.setStartingDate(privateDiscount.getStartingDate());
        dto.setEndingDate(privateDiscount.getEndingDate());
        dto.setPercentageDiscount(privateDiscount.getPercentageDiscount());
        dto.setActive(privateDiscount.isActive());

        dto.setCustomerEmail(privateDiscount.getCustomer().getEmail());
        dto.setPromoCode(privateDiscount.getPromoCode());
        dto.setStacking(privateDiscount.isStacking());
        dto.setApplied(privateDiscount.isApplied());

        return dto;
    }

    private CommonDiscountDto commonDiscountToDtoConverter(CommonDiscount commonDiscount) {
        CommonDiscountDto dto = new CommonDiscountDto();
        dto.setProductServiceName(commonDiscount.getProduct().getServiceName());
        dto.setProductName(commonDiscount.getProduct().getName());
        dto.setStartingDate(commonDiscount.getStartingDate());
        dto.setEndingDate(commonDiscount.getEndingDate());
        dto.setPercentageDiscount(commonDiscount.getPercentageDiscount());
        dto.setActive(commonDiscount.isActive());

        dto.setStacking(commonDiscount.isStacking());
        dto.setPromoCode(commonDiscount.getPromoCode());
        dto.setNumberOfAvailable(commonDiscount.getNumberOfAvailable());

        return dto;
    }

    public void saveDiscount(DiscountDto dto) {
        Discount d = discountRepository.getByProduct_ServiceName(dto.getProductServiceName());

        if (d == null) {
            d = new Discount();
            d.setProduct(productService.getProduct(dto.getProductServiceName()));
        }

        d.setStartingDate(dto.getStartingDate());
        d.setEndingDate(dto.getEndingDate());
        d.setPercentageDiscount(dto.getPercentageDiscount());
        d.setActive(dto.isActive());

        discountRepository.save(d);
    }

    public void saveCommonDiscount(CommonDiscountDto dto) {
        CommonDiscount d = commonDiscountRepository.getByProduct_ServiceName(dto.getProductServiceName());

        if (d == null) {
            d = new CommonDiscount();
            d.setProduct(productService.getProduct(dto.getProductServiceName()));
        }

        d.setStartingDate(dto.getStartingDate());
        d.setEndingDate(dto.getEndingDate());
        d.setPercentageDiscount(dto.getPercentageDiscount());
        d.setActive(dto.isActive());

        d.setNumberOfAvailable(dto.getNumberOfAvailable());
        d.setPromoCode(dto.getPromoCode());
        d.setStacking(dto.isStacking());

        commonDiscountRepository.save(d);
    }

    public void savePrivateDiscount(PrivateDiscountDto dto) {
        PrivateDiscount d = privateDiscountRepository.getByProduct_ServiceNameAndCustomer_Email(dto.getProductServiceName(),
                dto.getCustomerEmail());

        if (d == null) {
            d = new PrivateDiscount();
            d.setProduct(productService.getProduct(dto.getProductServiceName()));
            d.setCustomer(customerService.getCustomer(dto.getCustomerEmail()));
            d.setApplied(false);
        }

        d.setStartingDate(dto.getStartingDate());
        d.setEndingDate(dto.getEndingDate());
        d.setPercentageDiscount(dto.getPercentageDiscount());
        d.setActive(dto.isActive());

        d.setPromoCode(dto.getPromoCode());
        d.setStacking(dto.isStacking());
        d.setApplied(false);

        privateDiscountRepository.save(d);
    }

    public BigDecimal getPercentageDiscount(String productServiceName) {
        Discount d = discountRepository.getDiscountByProductServiceName(productServiceName);

        if (d != null && d.isActive() & LocalDateTime.now().isAfter(d.getStartingDate()) & LocalDateTime.now().isBefore(d.getEndingDate())) {
            return BigDecimal.valueOf(d.getPercentageDiscount());
        }
        return BigDecimal.ZERO;
    }

    public boolean existsPrivateDiscount(String promoCode, String productServiceName, String email) {
        return privateDiscountRepository.existsByPromoCodeAndProduct_ServiceNameAndCustomer_Email(promoCode, productServiceName, email);
    }

    public boolean existsCommonDiscount(String promoCode, String productServiceName) {
        return commonDiscountRepository.existsByPromoCodeAndProduct_ServiceName(promoCode, productServiceName);
    }

    public BigDecimal totalDiscountHandler(BigDecimal totalDiscount, String productServiceName, String promoCode, String email) {
        PrivateDiscount d = privateDiscountRepository.getByProduct_ServiceNameAndCustomer_Email(productServiceName, email);
        checkTimeRange(d.getStartingDate(), d.getEndingDate());
        checkActiveDiscount(d.isActive());
        checkPromoCode(d.getPromoCode(), promoCode);
        checkApplied(d.isApplied());

        updateAppliedStatusDiscount(d, true);
        if (d.isStacking()) {
            return totalDiscount.add(BigDecimal.valueOf(d.getPercentageDiscount()));
        } else {
            return BigDecimal.valueOf(d.getPercentageDiscount());
        }
    }

    public PrivateDiscount getPrivateDiscount(String productServiceName, String email) {
        return privateDiscountRepository.getByProduct_ServiceNameAndCustomer_Email(productServiceName, email);
    }

    public BigDecimal totalDiscountHandler(BigDecimal totalDiscount, String productServiceName, String promoCode) {
        CommonDiscount d = commonDiscountRepository.getByProduct_ServiceName(productServiceName);
        checkTimeRange(d.getStartingDate(), d.getEndingDate());
        checkActiveDiscount(d.isActive());
        checkPromoCode(d.getPromoCode(), promoCode);
        checkAvailableNumberDiscount(d);

        decreaseAvailableNumber(d, 1);
        if (d.isStacking()) {
            return totalDiscount.add(BigDecimal.valueOf(d.getPercentageDiscount()));
        } else {
            return BigDecimal.valueOf(d.getPercentageDiscount());
        }
    }

    public CommonDiscount getCommonDiscount(String productServiceName) {
        return commonDiscountRepository.getByProduct_ServiceName(productServiceName);
    }

    private void checkTimeRange(LocalDateTime startingDate, LocalDateTime endingDate) {
        if (LocalDateTime.now().isBefore(startingDate))
            throw new DiscountServiceException(HttpStatus.BAD_REQUEST, "The discount period has not yet come.");
        if (LocalDateTime.now().isAfter(endingDate))
            throw new DiscountServiceException(HttpStatus.BAD_REQUEST, "The discount period has already passed.");
    }

    private void checkActiveDiscount(boolean isActive) {
        if (!isActive)
            throw new DiscountServiceException(HttpStatus.BAD_REQUEST, "The discount is not active.");
    }

    private void checkPromoCode(String promoCodeFromDB, String promoCode) {
        if (!promoCodeFromDB.equals(promoCode))
            throw new DiscountServiceException(HttpStatus.BAD_REQUEST, "The promo code " + promoCode + " is wrong.");
    }

    private void checkAvailableNumberDiscount(CommonDiscount d) {
        if (d.getNumberOfAvailable() <= 0)
            throw new DiscountServiceException(HttpStatus.BAD_REQUEST, "Available promo codes are over.");
    }

    private void decreaseAvailableNumber(CommonDiscount d, int number) {
        d.setNumberOfAvailable(d.getNumberOfAvailable() - number);
        commonDiscountRepository.save(d);
    }

    public void increaseAvailableNumber(CommonDiscount d, int number) {
        d.setNumberOfAvailable(d.getNumberOfAvailable() + number);
        commonDiscountRepository.save(d);
    }

    private void checkApplied(boolean applied) {
        if (applied)
            throw new DiscountServiceException(HttpStatus.BAD_REQUEST, "This personal discount has already been applied.");
    }

    public void updateAppliedStatusDiscount(PrivateDiscount d, boolean status) {
        privateDiscountRepository.updateIsAppliedById(status, d.getId());
    }

}
