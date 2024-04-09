package dev.danielarrais.retryqueuesrabbit.broker.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Validated
@Component
@ConfigurationProperties("broker")
public class QueuesProperties {

    private Map<String, Queue> queues;
    private Map<String, Exchange> exchanges;

    public List<Queue> getQueues() {
        if (CollectionUtils.isEmpty(queues)) return new ArrayList<>();
        return new ArrayList<>(queues.values());
    }

    public List<Exchange> getExchanges() {
        if (CollectionUtils.isEmpty(exchanges)) return new ArrayList<>();
        return new ArrayList<>(exchanges.values());
    }

    @Getter
    @Setter
    @Validated
    public static class Queue {
        @NotBlank
        private String name;
        private String exchange;
        private int tll;
        private WaitQueue waitQueue;
        private DeadLetterQueue deadLetterQueue;
    }

    @Data
    @Validated
    public static class WaitQueue {
        @NotBlank
        private String name;
        private String exchange;
        private int ttl;
    }

    @Data
    @Validated
    public static class DeadLetterQueue {
        @NotBlank
        private String name;
        private int tll;
    }

    @Data
    @Validated
    public static class Exchange {
        @NotBlank
        private String name;
        private String type;
    }
}