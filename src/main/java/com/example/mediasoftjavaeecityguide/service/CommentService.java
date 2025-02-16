package com.example.mediasoftjavaeecityguide.service;

import com.example.mediasoftjavaeecityguide.model.Comment;
import com.example.mediasoftjavaeecityguide.model.Location;
import com.example.mediasoftjavaeecityguide.model.User;
import com.example.mediasoftjavaeecityguide.repository.CommentRepository;
import com.example.mediasoftjavaeecityguide.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final LocationRepository locationRepository;

    private final UserService userService;

    public List<Comment> findByLocationName(String locationName) {
        return commentRepository.findByLocation_Name(locationName);
    }

    public Comment save(String username, String locationName, String content) {
        Location location = locationRepository.findByName(locationName).orElseThrow();

        Comment newComment = new Comment();
        newComment.setId(null);
        newComment.setUser((User) userService.loadUserByUsername(username));
        newComment.setLocation(location);
        newComment.setContent(content);

        return commentRepository.save(newComment);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
