package top.shop.backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.shop.backend.dto.CatalogueDto;

@Configuration
public class BeanConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CatalogueDto catalogueDto() {
        return new CatalogueDto();
    }

}
