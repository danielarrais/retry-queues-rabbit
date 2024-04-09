package dev.danielarrais.retryqueuesrabbit.broker.setup.exchanges;

import dev.danielarrais.retryqueuesrabbit.broker.config.QueuesProperties;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ExchangesSetup {

    private final QueuesProperties brokerProperties;

    public ExchangesSetup(QueuesProperties brokerProperties) {
        this.brokerProperties = brokerProperties;
    }

    @Bean
    @Primary
    public Exchange exchange() {
        var name = brokerProperties.getExchanges().get(0).getName();
        return ExchangeBuilder.fanoutExchange(name)
                .build();
    }

    @Bean
    public Exchange exchangeRetryWait() {
        var name = brokerProperties.getExchanges().get(1).getName();
        return ExchangeBuilder.directExchange(name)
                .build();
    }
}