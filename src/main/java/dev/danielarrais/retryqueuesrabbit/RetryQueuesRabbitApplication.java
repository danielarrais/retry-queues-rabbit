package dev.danielarrais.retryqueuesrabbit;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class RetryQueuesRabbitApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(RetryQueuesRabbitApplication.class, args);
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i < 2; i++) {
            rabbitTemplate.convertAndSend("retry.exchange", "", "Teste " + i + "!");
            Thread.sleep(5000);
        }
    }
}
