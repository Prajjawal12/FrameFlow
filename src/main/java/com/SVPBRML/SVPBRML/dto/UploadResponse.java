package com.SVPBRML.SVPBRML.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResponse {
  private List<String> results;
  private String errorMessage;
  private String failedFileName;

}
