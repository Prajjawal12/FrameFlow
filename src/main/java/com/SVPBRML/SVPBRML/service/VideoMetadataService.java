package com.SVPBRML.SVPBRML.service;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SVPBRML.SVPBRML.model.Video;
import com.SVPBRML.SVPBRML.repository.VideoRepository;
import com.SVPBRML.SVPBRML.validator.VideoFileValidator;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class VideoMetadataService {
  @Autowired
  private VideoRepository videoRepository; // Remove static

  @Async
  public CompletableFuture<String> processVideoAsync(MultipartFile file) throws Exception { // Remove static
    try {
      Video video = extractAndSaveMetaData(file);
      return CompletableFuture.completedFuture("Processed video: " + video.getFileName());
    } catch (Exception e) {
      System.err.println("Error processing video: " + file.getOriginalFilename() + " - " + e.getMessage());
      return CompletableFuture
          .completedFuture("Error processing video: " + file.getOriginalFilename() + " - " + e.getMessage());
    }
  }

  public Video extractAndSaveMetaData(MultipartFile file) throws IOException {
    Video video = new Video();
    video.setFileName(file.getOriginalFilename());
    video.setFileSize(file.getSize());

    if (!VideoFileValidator.isValidFileType(file.getOriginalFilename())) {
      throw new IOException("Invalid file type.");
    }

    CompletableFuture<Void> resolutionFuture = CompletableFuture.supplyAsync(() -> {
      try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file.getInputStream())) {
        grabber.start();
        String contentType = grabber.getFormat();
        String fileExtension = getFileExtension(file.getOriginalFilename());

        if (isContentTypeMatching(contentType, fileExtension)) {
          video.setFileExtension(fileExtension);
        } else {
          throw new RuntimeException("Content type and file extension do not match.");
        }

        video.setDuration(grabber.getLengthInTime() / 1000 + " seconds");
        video.setContentType(contentType);
        video.setFrameRate(grabber.getFrameRate() + " fps");
        video.setResolution(grabber.getImageWidth() + "x" + grabber.getImageHeight());

        grabber.stop();
      } catch (Exception e) {
        throw new RuntimeException("Failed to extract metadata from the video: " + e.getMessage());
      }
      return null;
    });

    CompletableFuture.allOf(resolutionFuture).join();
    return videoRepository.save(video); // This will now work correctly
  }

  private String getFileExtension(String fileName) {
    if (fileName.lastIndexOf(".") != -1) {
      return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
    return "";
  }

  private boolean isContentTypeMatching(String contentType, String fileExtension) {
    switch (fileExtension) {
      case "mp4":
        return contentType.contains("mp4");
      case "avi":
        return contentType.contains("x-msvideo");
      case "mkv":
        return contentType.contains("x-matroska");
      case "mov":
        return contentType.contains("quicktime");
      case "flv":
        return contentType.contains("x-flv");
      case "mpeg":
        return contentType.contains("mpeg");
      case "wmv":
        return contentType.contains("x-ms-wmv");
      default:
        return false;
    }
  }
}
