package com.SVPBRML.SVPBRML.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SVPBRML.SVPBRML.validator.VideoFileValidator;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

  @PostMapping("/upload")
  public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
    String fileName = file.getOriginalFilename();
    if (fileName == null || VideoFileValidator.isValidFileType(fileName)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Invalid File Type,Allowed Filetypes are: " + VideoFileValidator.getAllowedFileTypes());
    }
    if (!VideoFileValidator.isValidFileSize(file.getSize())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size exceeds the maximum limit of 100MB");
    }
    return ResponseEntity.ok("File Uploaded Succesfully " + file.getOriginalFilename());
  }
}
