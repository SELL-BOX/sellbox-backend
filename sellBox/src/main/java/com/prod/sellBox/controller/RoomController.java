package com.prod.sellBox.controller;

import com.prod.sellBox.domain.RoomInfo;
import com.prod.sellBox.dto.RoomDto;
import com.prod.sellBox.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomRepository roomRepository;
    private static int sequence = 0;

    @PostMapping
    public RoomInfo createRoom(@RequestBody RoomDto room) {
        log.info("create Room : {}", room.toString());
        RoomInfo newRoom = RoomInfo.builder()
                        .id(++sequence)
                        .roomId(UUID.randomUUID().toString())
                        .roomName(room.getRoomName())
                        .hostId("test" + sequence)
                        .build();

        roomRepository.save(newRoom);

        return newRoom;
    }

    @GetMapping
    public List<RoomInfo> roomList() {
        log.info("request roomList");
        return roomRepository.findAll();
    }

    @GetMapping("/{roomId}")
    public RoomInfo enterRoom(@PathVariable String roomId) {
        log.info("request enterRoom : {}", roomId);
        return roomRepository.findById(roomId);
    }

    @DeleteMapping("/{roomId}")
    public String deleteRoom(@PathVariable String roomId) {
        log.info("request deleteRoom : {}", roomId);
        return roomRepository.deleteById(roomId).getMsg();
    }
}
