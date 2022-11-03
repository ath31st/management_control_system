package top.shop.shop1_service.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Collections;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

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