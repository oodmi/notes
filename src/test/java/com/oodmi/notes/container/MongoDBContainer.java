package com.oodmi.notes.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public abstract class MongoDBContainer {

    public static final Integer PORT = 27017;

    public static final org.testcontainers.containers.MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new org.testcontainers.containers.MongoDBContainer("mongo:latest")
                .withStartupTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .withExposedPorts(PORT);
        mongoDBContainer.start();
    }

    public static void apply(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of(
                        "spring.data.mongodb.host=" + mongoDBContainer.getHost(),
                        "spring.data.mongodb.port=" + mongoDBContainer.getMappedPort(PORT)
                )
                .applyTo(configurableApplicationContext.getEnvironment());
    }
}
