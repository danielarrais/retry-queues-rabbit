package dev.danielarrais.retryqueuesrabbit.broker.setup.queues;

import dev.danielarrais.retryqueuesrabbit.broker.config.QueuesProperties;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueRetrySetup {

    private final QueuesProperties.Queue queue;

    public QueueRetrySetup(@Qualifier("retryQueueSample") QueuesProperties.Queue retryQueueSample) {
        this.queue = retryQueueSample;
    }

    @Bean
    public Queue queueRetry() {
        return QueueBuilder.durable(queue.getName())
                .deadLetterExchange(queue.getWaitQueue().getExchange())
                .deadLetterRoutingKey(queue.getWaitQueue().getName())
                .build();
    }

    @Bean
    public Queue waitQueueRetry() {
        return QueueBuilder.durable(queue.getWaitQueue().getName())
                .deadLetterRoutingKey(queue.getName())
                .deadLetterExchange(queue.getWaitQueue().getExchange())
                .ttl(queue.getWaitQueue().getTtl())
                .build();
    }

    @Bean
    public Queue deadLetterQueueRetry() {
        return new Queue(queue.getDeadLetterQueue().getName());
    }

    @Bean
    Binding queueRetryBind(Queue queueRetry, Exchange exchange) {
        return BindingBuilder.bind(queueRetry)
                .to(exchange).with("").noargs();
    }

    @Bean
    public Binding buildQueueRetryBind(Queue queueRetry,
                                       @Qualifier("exchangeRetryWait") Exchange exchange) {
        return BindingBuilder.bind(queueRetry)
                .to(exchange)
                .with(queue.getName())
                .noargs();
    }
    
    @Bean
    public Binding buildWaitQueueRetryBind(Queue waitQueueRetry,
                                           @Qualifier("exchangeRetryWait") Exchange exchange) {
        return BindingBuilder.bind(waitQueueRetry)
                .to(exchange)
                .with(queue.getWaitQueue().getName())
                .noargs();
    }

    @Bean
    public Binding buildDeadLetterQueueRetryBind(Queue deadLetterQueueRetry,
                                                 @Qualifier("exchangeRetryWait") Exchange exchange) {
        return BindingBuilder.bind(deadLetterQueueRetry)
                .to(exchange)
                .with(deadLetterQueueRetry.getName())
                .noargs();
    }
}