package com.prod.sellBox.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@RequiredArgsConstructor
public class Thumbnail {

    @Id @Column(name = "thumbnail_id")
    String thumbnailId;
    byte[] imageSource;

    public Thumbnail(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public void setImageSource(byte[] imageSource) {
        this.imageSource = imageSource;
    }
}
