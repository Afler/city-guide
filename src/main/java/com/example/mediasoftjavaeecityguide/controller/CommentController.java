package com.example.mediasoftjavaeecityguide.controller;

import com.example.mediasoftjavaeecityguide.model.Comment;
import com.example.mediasoftjavaeecityguide.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @SpringSwaggerEndpoint(
            method = RequestMethod.POST,
            path = "/save",
            description = "Добавить отзыв о локации от имени текущего пользователя."
    )
    public Comment save(@RequestParam String locationName,
                        @RequestParam String content,
                        Authentication authentication) {
        return commentService.save(authentication.getName(), locationName, content);
    }

    @SpringSwaggerEndpoint(
            method = RequestMethod.GET,
            path = "/findByLocation",
            description = "Получить все отзывы о локации по её названию."
    )
    public List<Comment> findByLocationName(@RequestParam String locationName) {
        return commentService.findByLocationName(locationName);
    }
}
