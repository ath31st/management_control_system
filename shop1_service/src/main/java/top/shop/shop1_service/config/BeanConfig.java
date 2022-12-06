package top.shop.shop1_service.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    @Value("${spring.data.mongodb.database}")
    private String databaseName;
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private String port;
    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.password}")
    private String password;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString("mongodb://" + host + ":" + port + "/" + databaseName);
        MongoCredential mongoCredential = MongoCredential.createCredential(username, databaseName, password.toCharArray());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(mongoCredential)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), databaseName);
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