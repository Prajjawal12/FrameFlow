package com.SVPBRML.SVPBRML.controller;

import com.SVPBRML.SVPBRML.dto.UploadResponse;
import com.SVPBRML.SVPBRML.service.ProducerService;
import com.SVPBRML.SVPBRML.service.VideoMetadataService;
import com.SVPBRML.SVPBRML.validator.VideoFileValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

  @Autowired
  private ProducerService producerService;

  @Autowired
  private VideoMetadataService videoMetadataService;

  @PostMapping("/upload")
  public ResponseEntity<UploadResponse> uploadMultipleVideos(@RequestParam("files") MultipartFile[] files)
      throws Exception {
    List<CompletableFuture<String>> futures = new ArrayList<>();
    List<String> results = new ArrayList<>();
    List<String> failedFiles = new ArrayList<>();

    for (MultipartFile file : files) {
      String fileName = file.getOriginalFilename();

      if (fileName == null || !VideoFileValidator.isValidFileType(fileName)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new UploadResponse(List.of(),
                "Invalid File Type. Allowed file types are: " + VideoFileValidator.getAllowedFileTypes(), fileName));
      }
      producerService.sendVideoProcessingJob(fileName);
      futures.add(videoMetadataService.processVideoAsync(file));
    }

    for (CompletableFuture<String> future : futures) {
      try {
        results.add(future.join());
      } catch (Exception e) {
        String failedFileName = e.getMessage();
        failedFiles.add(failedFileName);
        System.err.println("Error processing video: " + e.getMessage());
        results.add("Error processing video: " + e.getMessage());
      }
    }

    String errorMessage = "";
    if (!failedFiles.isEmpty()) {
      errorMessage = "File(s) exceed the maximum allowed size of 10 MB: " + String.join(", ", failedFiles);
    }

    UploadResponse response = new UploadResponse(results, errorMessage, "There were no failed files!");
    return ResponseEntity.ok(response);
  }

}
