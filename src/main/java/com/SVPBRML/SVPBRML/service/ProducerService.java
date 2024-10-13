package com.SVPBRML.SVPBRML.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
  private final RabbitTemplate rabbitTemplate;
  private final String QUEUE_NAME = "videoProcessingQueue";

  public ProducerService(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendVideoProcessingJob(String videoPath) {
    rabbitTemplate.convertAndSend(QUEUE_NAME, videoPath);
    System.out.println("Video enqueued for processing: " + videoPath);
  }
}
