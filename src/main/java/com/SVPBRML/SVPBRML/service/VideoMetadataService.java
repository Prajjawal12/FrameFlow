package com.SVPBRML.SVPBRML.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.SVPBRML.SVPBRML.model.Video;
import com.SVPBRML.SVPBRML.repository.VideoRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class VideoMetadataService {
  @Autowired
  private VideoRepository videoRepository;

  public Video extractAndSaveMetaData(MultipartFile file) throws IOException, SAXException, TikaException {
    Tika tika = new Tika();
    Metadata metadata = new Metadata();
    BodyContentHandler bodyContentHandler = new BodyContentHandler();
    AutoDetectParser parser = new AutoDetectParser();

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
      parser.parse(inputStream, bodyContentHandler, metadata, new ParseContext());
    } catch (IOException e) {
      throw new IOException("Failed to extract metadata from the video: " + e.getMessage(), e);
    } catch (TikaException | SAXException e) {
      throw new IOException("Failed to parse video metadata: " + e.getMessage(), e);
    }

    Video video = new Video();
    video.setFileName(file.getOriginalFilename());
    video.setFileSize(file.getSize());
    video.setContentType(tika.detect(file.getBytes()));

    String width = metadata.get("tiff:ImageWidth");
    String height = metadata.get("tiff:ImageLength");
    video.setResolution((width != null && height != null) ? width + "x" + height : "Unknown");

    String duration = metadata.get("xmpDM:duration");
    video.setDuration(duration != null ? duration + "ms" : "Unknown");

    String frameRate = metadata.get("xmpDM:videoFrameRate");
    video.setFrameRate(frameRate != null ? frameRate + "fps" : "Unknown");

    return videoRepository.save(video);
  }

}
