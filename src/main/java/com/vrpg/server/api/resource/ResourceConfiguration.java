package com.vrpg.server.api.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ResourceConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceConfiguration.class);

    private final ApplicationContext context;

    ResourceConfiguration(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    @ConditionalOnMissingBean
    ImageResourceManager dummyImageResourceManager() {
        LOGGER.info("{} environmental variable is not set. Dummy ResourceManager is being used!",
                CloudinaryResourceManager.PROPERTY);
        return (name, extension) -> {
            LOGGER.trace("getResource - {}, {}", name, extension);
            return context.getResource("classpath:dummy.png");
        };
    }

    @Bean
    @ConditionalOnMissingBean
    MeshResourceManager dummyMeshResourceController() {
        return (name, extension) -> {
            LOGGER.trace("getResource - {}, {}", name, extension);
            return context.getResource("classpath:" + name + "." + extension);
        };
    }
}
