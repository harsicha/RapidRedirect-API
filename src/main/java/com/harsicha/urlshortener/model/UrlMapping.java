package com.harsicha.urlshortener.model;

import com.harsicha.urlshortener.utility.Constants;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(indexes = {
    @Index(name = "in_random", columnList = "Random", unique = true)
})
public class UrlMapping {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(unique = true, nullable = false, length = Constants.SHORT_LENGTH)
    private String random;

    @Column(nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
