package com.SVPBRML.SVPBRML.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import com.SVPBRML.SVPBRML.dto.UploadResponse;

import java.util.ArrayList;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<UploadResponse> handleMaxSizeException(MaxUploadSizeExceededException e) {
    String errorMessage = "One or more files exceed the maximum allowed size of 10 MB.";
    UploadResponse response = new UploadResponse(new ArrayList<>(), errorMessage, null);
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
  }

  @ExceptionHandler(MultipartException.class)
  public ResponseEntity<UploadResponse> handleMultipartException(MultipartException e) {
    String errorMessage = "File size exceeds the maximum allowed size. Please upload a smaller file.";
    UploadResponse response = new UploadResponse(new ArrayList<>(), errorMessage, null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
