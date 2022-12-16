package top.shop.shop1_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Shop1ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Shop1ServiceApplication.class, args);
	}

}
