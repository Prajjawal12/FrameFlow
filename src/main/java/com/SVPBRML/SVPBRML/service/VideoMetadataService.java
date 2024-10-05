package com.SVPBRML.SVPBRML.service;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SVPBRML.SVPBRML.model.Video;
import com.SVPBRML.SVPBRML.repository.VideoRepository;

import java.io.IOException;

@Service
public class VideoMetadataService {
  @Autowired
  private VideoRepository videoRepository;

  public Video extractAndSaveMetaData(MultipartFile file) throws IOException {

    Video video = new Video();
    video.setFileName(file.getOriginalFilename());
    video.setFileSize(file.getSize());

    try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file.getInputStream())) {
      grabber.start();
      video.setDuration(grabber.getLengthInTime() / 1000 + " seconds");
      video.setContentType(grabber.getFormat());
      video.setFrameRate(String.valueOf(grabber.getFrameRate() + "fps"));
      video.setResolution(grabber.getImageWidth() + "x" + grabber.getImageHeight());
      grabber.stop();

    } catch (Exception e) {
      throw new IOException("Failed to extract metadata from the video: " + e.getMessage());
    }

    return videoRepository.save(video);
  }

}
