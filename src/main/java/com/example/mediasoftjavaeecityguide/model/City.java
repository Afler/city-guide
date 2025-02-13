package com.example.mediasoftjavaeecityguide.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "city")
public class City extends BaseEntity {

    @Column(name = "name")
    public String name;
}
