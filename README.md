# RabbitMQ retry messages with Spring Boot

The solution implemented in this repository is based in this [article](https://programmerfriend.com/rabbit-mq-retry/).

## The problem

Implementing a retry flow for rabbitmq for messages coming from a fanout exchange.

## Solution

To use DLQ Routing for create a retry system using the native resources of rabbitmq:


![image](https://github.com/danielarrais/retry-queues-rabbit/assets/28496479/b58950e8-5af9-4aa6-a0bf-fe413e4f8878)
