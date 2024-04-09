package dev.danielarrais.retryqueuesrabbit.broker.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class Consumers {

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.queues.retry-queue-sample.dead-letter-queue.name}")
    private String dlQueue;

    @Value("${broker.queues.retry-two-queue-sample.dead-letter-queue.name}")
    private String dlQueueTwo;

    @RabbitListener(queues = "${broker.queues.retry-queue-sample.name}")
    public void consumerRetry(Message message) throws Exception {
        processMessage(message, dlQueue);
    }

    @RabbitListener(queues = "${broker.queues.retry-two-queue-sample.name}")
    public void consumerRetryTwo(Message message) throws Exception {
        processMessage(message, dlQueueTwo);
    }

    private boolean hasExceededRetryCount(Message in) {
        var xDeathHeader = in.getMessageProperties().getXDeathHeader();
        var queueName = in.getMessageProperties().getConsumerQueue();

        if (xDeathHeader != null && !xDeathHeader.isEmpty()) {
            Long count = (Long) xDeathHeader.get(0).get("count");
            log.info("Attempt read queue '{}' number {}", queueName, count);
            return count >= 3;
        }

        return false;
    }

    public void processMessage(Message message, String dlQueue) throws Exception {
        try {
            var queueName = message.getMessageProperties().getConsumerQueue();
            log.info("Message read from {}: {}", queueName, new String(message.getBody()));

            throw new Exception("consumerRetry: Proposital Error!");
        } catch (Exception e) {
            if (hasExceededRetryCount(message)) {
                putIntoDeadLetter(message, dlQueue);
            } else {
                throw e;
            }
        }
    }

    private void putIntoDeadLetter(Message failedMessage, String dlQueue) {
        var queueName = failedMessage.getMessageProperties().getConsumerQueue();
        log.info("Message read from {} sended to {}", queueName, dlQueue);
        this.rabbitTemplate.send(dlQueue, failedMessage);
    }
}
