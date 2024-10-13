package com.SVPBRML.SVPBRML.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "videos")
@Data
public class Video {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private long fileSize;

  private String resolution;

  private String duration;

  private LocalDateTime uploadedAt;

  private String contentType;

  private String frameRate;

  private String fileExtension;

  @PrePersist
  public void prePersist() {
    this.uploadedAt = LocalDateTime.now();
  }
}
