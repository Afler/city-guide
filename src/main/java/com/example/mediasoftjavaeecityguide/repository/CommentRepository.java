package com.example.mediasoftjavaeecityguide.repository;

import com.example.mediasoftjavaeecityguide.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByLocation_Name(String locationName);
}
