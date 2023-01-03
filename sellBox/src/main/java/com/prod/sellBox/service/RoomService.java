package com.prod.sellBox.service;

import com.prod.sellBox.domain.RoomInfo;
import com.prod.sellBox.domain.Thumbnail;
import com.prod.sellBox.repository.RoomRepository;
import com.prod.sellBox.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ThumbnailRepository thumbnailRepository;

    public RoomInfo save(RoomInfo room) {
        return roomRepository.save(room);
    }

    public List<RoomInfo> findAll() {
        return roomRepository.findAll();
    }

    public RoomInfo findById(Long roomId) {
        return roomRepository.findById(roomId).get();
    }

    public void deleteById(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    public void updateThumbnail(Thumbnail thumbnail) {
        Thumbnail tmpThumbnail = thumbnailRepository.findById(thumbnail.getThumbnailId()).get();
        thumbnail.setImageSource(tmpThumbnail.getImageSource());
    }

    public Thumbnail getThumbnail(String thumbnailId) {
        if (thumbnailRepository.existsById(thumbnailId)) {
            return thumbnailRepository.findById(thumbnailId).get();
        }
        return null;
    }
}
