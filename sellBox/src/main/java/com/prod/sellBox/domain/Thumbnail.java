package com.prod.sellBox.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Getter
@RequiredArgsConstructor
public class Thumbnail {

    @Id @Column(name = "thumbnail_id")
    String thumbnailId;
    @Lob
    byte[] imageSource;

    public Thumbnail(String thumbnailId, byte[] imageSource) {
        this.thumbnailId = thumbnailId;
        this.imageSource = imageSource;
    }

    public void setImageSource(byte[] imageSource) {
        this.imageSource = imageSource;
    }
}
