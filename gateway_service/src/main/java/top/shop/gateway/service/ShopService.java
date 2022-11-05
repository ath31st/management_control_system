package top.shop.gateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.ShopDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopService {
    @Value("${backend.url}")
    private String backendUrl;

    public List<ShopDto> getShops() {
        RestTemplate restTemplate = new RestTemplate();
        String url = backendUrl + "/api/shops";
        ShopDto[] shops = restTemplate.getForObject(url, ShopDto[].class);

        if (shops == null) return new ArrayList<>();

        return new ArrayList<>(List.of(shops));
    }

}
