package com.elearning.configurationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigurationServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConfigurationServerApplication.class, args);
	}

}
//docker run -d -p 9411:9411 openzipkin/zipkin
