package com.SVPBRML.SVPBRML.controller;

import com.SVPBRML.SVPBRML.model.Video;
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
import org.xml.sax.SAXException;

import java.io.IOException;

@RestController
@RequestMapping("/api/videos")
public class VideoController {
  @Autowired
  private VideoMetadataService videoMetadataService;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file)
      throws IOException, SAXException {
    String fileName = file.getOriginalFilename();

    if (fileName == null || !VideoFileValidator.isValidFileType(fileName)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Invalid File Type. Allowed file types are: " + VideoFileValidator.getAllowedFileTypes());
    }

    if (!VideoFileValidator.isValidFileSize(file.getSize())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size exceeds the maximum limit of 10 MB");
    }

    try {
      Video savedVideo = videoMetadataService.extractAndSaveMetaData(file);

      String response = "File uploaded successfully. Saved video ID: " + savedVideo.getId();
      return ResponseEntity.ok(response);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process video: " + e.getMessage());
    }
  }

}
