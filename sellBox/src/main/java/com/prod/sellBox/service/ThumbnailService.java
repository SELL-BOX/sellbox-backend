package com.prod.sellBox.service;

import com.prod.sellBox.domain.Thumbnail;
import com.prod.sellBox.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ThumbnailService {

    private final ThumbnailRepository thumbnailRepository;

    public Thumbnail getThumbnail(String thumbnailId) {
        if (thumbnailRepository.existsById(thumbnailId)) {
            return thumbnailRepository.findById(thumbnailId).get();
        }
        return null;
    }

    public void updateThumbnail(Thumbnail thumbnail) {
        Thumbnail tmpThumbnail = thumbnailRepository.findById(thumbnail.getThumbnailId()).get();
        thumbnail.setImageSource(tmpThumbnail.getImageSource());
    }

    public void deleteThumbnail(String thumbnailId) {
        if (thumbnailRepository.existsById(thumbnailId)) {
           thumbnailRepository.deleteById(thumbnailId);
        } else {
            throw new IllegalStateException("존재하지 않는 썸네일 입니다.");
        }
    }
}
