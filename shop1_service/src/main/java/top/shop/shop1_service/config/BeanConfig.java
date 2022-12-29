package top.shop.shop1_service.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootStrapServers;
//    private final KafkaProperties kafkaProperties;
//
//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//        // get configs on application.properties/yml
//        kafkaProperties.setBootstrapServers(Collections.singletonList(bootStrapServers));
//        Map<String, Object> properties = kafkaProperties.buildProducerProperties();
//        return new DefaultKafkaProducerFactory<>(properties);
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    @Bean
//    public NewTopic topic() {
//        return TopicBuilder
//                .name("t.order")
//                .partitions(1)
//                .replicas(1)
//                .build();
//    }
}