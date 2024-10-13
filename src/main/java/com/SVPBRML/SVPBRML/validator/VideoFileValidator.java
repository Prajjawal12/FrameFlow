package com.SVPBRML.SVPBRML.validator;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class VideoFileValidator {
  private final static List<String> allowedFileTypes = Arrays.asList("mp4", "avi", "mkv", "mov", "flv", "mpeg", "wmv");
  private final static long MAX_FILE_SIZE = 10 * 1024 * 1024;

  public static boolean isValidFileType(String fileName) {
    if (fileName == null || fileName.lastIndexOf(".") == -1) {
      return false;
    }
    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    return allowedFileTypes.contains(fileExtension);
  }

  public static String validateFile(MultipartFile file) {
    String fileName = file.getOriginalFilename();

    if (!isValidFileType(fileName)) {
      return "Invalid file type. Allowed file types are: " + getAllowedFileTypes();
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      return "File size exceeds the maximum allowed size of 10 MB.";
    }

    return null;
  }

  public static List<String> getAllowedFileTypes() {
    return allowedFileTypes;
  }
}
