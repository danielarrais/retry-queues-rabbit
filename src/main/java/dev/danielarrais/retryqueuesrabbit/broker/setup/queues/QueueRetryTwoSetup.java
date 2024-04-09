package dev.danielarrais.retryqueuesrabbit.broker.setup.queues;

import dev.danielarrais.retryqueuesrabbit.broker.config.QueuesProperties;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueRetryTwoSetup {

    private final QueuesProperties.Queue queue;

    public QueueRetryTwoSetup(@Qualifier("retryTwoQueueSample") QueuesProperties.Queue retryTwoQueueSample) {
        this.queue = retryTwoQueueSample;
    }

    @Bean
    public Queue queueRetryTwo() {
        return QueueBuilder.durable(queue.getName())
                .deadLetterExchange(queue.getWaitQueue().getExchange())
                .deadLetterRoutingKey(queue.getWaitQueue().getName())
                .build();
    }

    @Bean
    public Queue waitQueueRetryTwo() {
        return QueueBuilder.durable(queue.getWaitQueue().getName())
                .deadLetterRoutingKey(queue.getName())
                .deadLetterExchange(queue.getWaitQueue().getExchange())
                .ttl(queue.getWaitQueue().getTtl())
                .build();
    }

    @Bean
    public Queue deadLetterQueueRetryTwo() {
        return new Queue(queue.getDeadLetterQueue().getName());
    }

    @Bean
    Binding queueRetryTwoBind(Queue queueRetryTwo, Exchange exchange) {
        return BindingBuilder.bind(queueRetryTwo)
                .to(exchange).with("").noargs();
    }

    @Bean
    public Binding buildQueueRetryTwoBind(Queue queueRetryTwo,
                                       @Qualifier("exchangeRetryWait") Exchange exchange) {
        return BindingBuilder.bind(queueRetryTwo)
                .to(exchange)
                .with(queue.getName())
                .noargs();
    }

    @Bean
    public Binding buildWaitQueueRetryTwoBind(Queue waitQueueRetryTwo,
                                          @Qualifier("exchangeRetryWait") Exchange exchange) {
        return BindingBuilder.bind(waitQueueRetryTwo)
                .to(exchange)
                .with(queue.getWaitQueue().getName())
                .noargs();
    }

    @Bean
    public Binding buildDeadLetterQueueRetryTwoBind(Queue deadLetterQueueRetryTwo,
                                                    @Qualifier("exchangeRetryWait") Exchange exchange) {
        return BindingBuilder.bind(deadLetterQueueRetryTwo)
                .to(exchange)
                .with(deadLetterQueueRetryTwo.getName())
                .noargs();
    }
}