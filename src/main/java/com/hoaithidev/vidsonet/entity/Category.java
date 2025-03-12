package com.hoaithidev.vidsonet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    Set<VideoCategory> videoCategories;
}
