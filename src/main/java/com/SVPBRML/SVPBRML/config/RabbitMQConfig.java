package com.SVPBRML.SVPBRML.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  public Queue videoProcessingQueue() {
    return new Queue("videoProcessingQueue", true);
  }
}