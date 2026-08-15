package com.bytebites.menuservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.google.cloud.spring.data.datastore.repository.config.EnableDatastoreRepositories;

@Configuration
@Profile("gcp")
@EnableDatastoreRepositories(basePackages = "com.bytebites.menuservice.repository")
public class DatastoreConfig {
}
