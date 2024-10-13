package com.SVPBRML.SVPBRML.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

  public void processVideo(String videoPath) {
    System.out.println("Received video for processing: " + videoPath);
    // processing simulation
    extractFramesFromVideo(videoPath);
  }

  @RabbitListener(queues = "videoProcessingQueue")
  private void extractFramesFromVideo(String videoPath) {
    // frames extraction logic goes here

    System.out.println("Extracting frames from: " + videoPath);

    try {
      Thread.sleep(3000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Frames extracted for video: " + videoPath);
  }
}
