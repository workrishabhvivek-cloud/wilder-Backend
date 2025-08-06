package com.wilderBackend.sections.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_key", nullable = false, unique = true)
    private String imageKey;

    @Column(name = "image_title", nullable = false)
    private String imageTitle;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @JsonProperty("imageKey")
    public void setImageKey(String imageKey) {
        if (!"banner".equalsIgnoreCase(imageKey) && !"carousel".equalsIgnoreCase(imageKey)) {
            throw new IllegalArgumentException("imageKey must be either 'banner' or 'carousel'");
        }
        this.imageKey = imageKey.toLowerCase();
    }
}
