package dev.danielarrais.retryqueuesrabbit.broker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class QueuesPropertiesConfig {
    @Primary
    @Bean("retryQueueSample")
    @ConfigurationProperties("broker.queues.retry-queue-sample")
    public QueuesProperties.Queue retryQueueSample() {
        return new QueuesProperties.Queue();
    }

    @Bean
    public QueuesProperties brokerProperties() {
        return new QueuesProperties();
    }

    @Bean("retryTwoQueueSample")
    @ConfigurationProperties("broker.queues.retry-two-queue-sample")
    public QueuesProperties.Queue retryTwoQueueSample() {
        return new QueuesProperties.Queue();
    }
}
