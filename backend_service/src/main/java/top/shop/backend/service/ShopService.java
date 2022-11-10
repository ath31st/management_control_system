package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.ShopDto;
import top.shop.backend.entity.Order;
import top.shop.backend.entity.Shop;
import top.shop.backend.exceptionhandler.exception.ShopException;
import top.shop.backend.repository.ShopRepository;
import top.shop.backend.service.event.BalanceEvent;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;

    public Shop getShop(String shopServiceName) {
        return shopRepository.getShop(shopServiceName).orElseThrow(
                () -> new ShopException(HttpStatus.NOT_FOUND, "Shop with name " + shopServiceName + " not found!"));
    }

    @EventListener
    public void changeBalance(BalanceEvent event) {
        Order order = (Order) event.getSource();
        Shop shop = getShop(order.getShop().getServiceName());
        shop.setBalance(shop.getBalance().add(order.getTotalPrice()));
        shopRepository.save(shop);
    }

    public void changeBalance(double payment, String shopName) {
        Shop shop = getShop(shopName);
        shop.setBalance(shop.getBalance().add(BigDecimal.valueOf(payment)));
        shopRepository.save(shop);
    }

    public void resetBalance(String shopName) {
        Shop shop = getShop(shopName);
        shop.setBalance(BigDecimal.ZERO);
        shopRepository.save(shop);
    }

    public ResponseEntity<HttpStatus> saveNewShop(ShopDto shopDto) {
        if (shopRepository.getShop(shopDto.getServiceName()).isPresent())
            throw new ShopException(HttpStatus.CONFLICT, "Shop with name " + shopDto.getName() + " already exists!");

        Shop shop = modelMapper.map(shopDto, Shop.class);
        shop.setBalance(BigDecimal.ZERO);

        shopRepository.save(shop);
        log.info("Shop with name {} created", shop.getServiceName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ShopDto getShopDto(String shopServiceName) {
        Shop shop = getShop(shopServiceName);
        return modelMapper.map(shop, ShopDto.class);
    }

    public List<ShopDto> getShopDtoList() {
        return shopRepository.findAll()
                .stream()
                .map(s -> modelMapper.map(s, ShopDto.class))
                .toList();
    }

    public ResponseEntity<HttpStatus> saveShopChanges(ShopDto shopDto) {
        Shop shop = getShop(shopDto.getServiceName());
        shop.setName(shopDto.getName());
        shop.setUrl(shopDto.getUrl());

        shopRepository.save(shop);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
