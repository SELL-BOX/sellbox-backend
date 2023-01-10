package com.prod.sellBox.service;

import com.prod.sellBox.domain.Thumbnail;
import com.prod.sellBox.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class ThumbnailService {

    private final ThumbnailRepository thumbnailRepository;

    public Thumbnail saveThumbnail(String thumbnailId, MultipartFile imgFile) throws IOException {
        Thumbnail thumbnail = new Thumbnail(thumbnailId, imgFile.getBytes());
        thumbnailRepository.save(thumbnail);

        return thumbnail;
    }

    public Thumbnail getThumbnail(String thumbnailId) {
        if (thumbnailRepository.existsById(thumbnailId)) {
            return thumbnailRepository.findById(thumbnailId).get();
        }
        return null;
    }

    public void updateThumbnail(Thumbnail thumbnail, MultipartFile imgFile) throws IOException {
        Thumbnail tmpThumbnail = thumbnailRepository.findById(thumbnail.getThumbnailId()).get();
        tmpThumbnail.setImageSource(imgFile.getBytes());
    }

    public void deleteThumbnail(String thumbnailId) {
        if (thumbnailRepository.existsById(thumbnailId)) {
           thumbnailRepository.deleteById(thumbnailId);
        } else {
            throw new IllegalStateException("존재하지 않는 썸네일 입니다.");
        }
    }
}
