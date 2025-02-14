package com.example.mediasoftjavaeecityguide.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "location_id")
    private Location location;
}
