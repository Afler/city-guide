package com.example.mediasoftjavaeecityguide;

import com.example.mediasoftjavaeecityguide.model.*;
import com.example.mediasoftjavaeecityguide.repository.CommentRepository;
import com.example.mediasoftjavaeecityguide.repository.LocationRepository;
import com.example.mediasoftjavaeecityguide.service.CommentService;
import com.example.mediasoftjavaeecityguide.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = {CommentService.class})
@ExtendWith(SpringExtension.class)
public class CommentServiceTest {

    public static final String TEST_CITY_NAME = "Test city";
    public static final String TEST_LOCATION_NAME = "Test location";
    public static final String TEST_COMMENT_CONTENT = "Test content";
    public static final String TEST_USERNAME = "Test user";
    public static final String TEST_USERNAME_PASSWORD = "Test pass";

    @MockitoBean
    private LocationRepository locationRepository;

    @MockitoBean
    private CommentRepository commentRepository;

    @MockitoBean
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        Mockito.when(locationRepository.findByName(TEST_LOCATION_NAME)).thenReturn(Optional.of(getLocation(getCity())));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(getComment());
        Mockito.when(commentRepository.findByLocation_Name(TEST_LOCATION_NAME)).thenReturn(List.of(getComment()));
    }


    private City getCity() {
        return new City(TEST_CITY_NAME);
    }

    private Location getLocation(City city) {
        return new Location(TEST_LOCATION_NAME,
                LocationCategory.ARCHITECTURE,
                new GeoPoint(0.0, 0.0),
                0.0,
                0,
                city,
                new HashSet<>());
    }

    private Comment getComment() {
        return new Comment(TEST_COMMENT_CONTENT, null, getLocation(getCity()));
    }

    @Test
    void commentService_SaveCommentFromParams_ReturnComment() {
        Comment savedComment = commentService.save(TEST_USERNAME, TEST_LOCATION_NAME, TEST_COMMENT_CONTENT);

        Assertions.assertNotNull(savedComment);
    }

    @Test
    void commentService_SaveComment_ReturnComment() {
        Comment commentToSave = getComment();

        Comment savedComment = commentService.save(commentToSave);

        Assertions.assertNotNull(savedComment);
    }

    @Test
    void commentService_FindByLocationName_ReturnComments() {
        List<Comment> foundComments = commentService.findByLocationName(TEST_LOCATION_NAME);

        Assertions.assertFalse(foundComments.isEmpty());
        Assertions.assertEquals(1, foundComments.size());
        Assertions.assertNotNull(foundComments.get(0));
    }
}
