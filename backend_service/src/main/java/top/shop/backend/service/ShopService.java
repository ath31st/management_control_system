package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<HttpStatus> saveNewShop(ShopDto shopDto) {
        if (shopRepository.getShop(shopDto.getName()).isPresent())
            throw new ShopException(HttpStatus.CONFLICT, "Shop with name " + shopDto.getName() + " already exists!");
        Shop shop = modelMapper.map(shopDto, Shop.class);
        shopRepository.save(shop);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<ShopDto> getShopDtoList() {
        return shopRepository.findAll()
                .stream()
                .map(s -> modelMapper.map(s, ShopDto.class))
                .toList();
    }

}
