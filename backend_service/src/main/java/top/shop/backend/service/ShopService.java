package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.ShopDto;
import top.shop.backend.entity.Shop;
import top.shop.backend.exceptionhandler.exception.ShopException;
import top.shop.backend.repository.ShopRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;

    public Shop getShop(String shopName) {
        return shopRepository.getShop(shopName).orElseThrow(
                () -> new ShopException(HttpStatus.NOT_FOUND, "Shop with name " + shopName + " not found!"));
    }

    public Shop saveNewShop(Shop shop) {
        if (shopRepository.getShop(shop.getName()).isPresent())
            throw new ShopException(HttpStatus.CONFLICT, "Shop with name " + shop.getName() + " already exists!");

        return shopRepository.save(shop);
    }

    public List<ShopDto> getShopDtoList() {
        return shopRepository.findAll()
                .stream()
                .map(s -> modelMapper.map(s, ShopDto.class))
                .toList();
    }

}
