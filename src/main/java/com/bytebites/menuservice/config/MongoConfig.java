package com.bytebites.menuservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("!gcp")
@EnableMongoRepositories(basePackages = "com.bytebites.menuservice.repository")
public class MongoConfig {
}
