package com.example.mediasoftjavaeecityguide.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "city")
@AllArgsConstructor
@NoArgsConstructor
public class City extends BaseEntity {

    @Column(name = "name")
    public String name;
}
