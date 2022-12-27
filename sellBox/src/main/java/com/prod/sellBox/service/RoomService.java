package com.prod.sellBox.service;

import com.prod.sellBox.domain.RoomInfo;
import com.prod.sellBox.repository.RoomRepository;
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

    public long save(RoomInfo room) {
        return roomRepository.save(room);
    }

    public List<RoomInfo> findAll() {
        return roomRepository.findAll();
    }

    public RoomInfo findById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public void deleteById(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
