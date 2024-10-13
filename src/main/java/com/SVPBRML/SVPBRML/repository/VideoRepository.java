package com.SVPBRML.SVPBRML.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SVPBRML.SVPBRML.model.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

}
