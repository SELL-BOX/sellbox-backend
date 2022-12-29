package com.prod.sellBox.controller;

import com.prod.sellBox.domain.RoomInfo;
import com.prod.sellBox.domain.UserEntity;
import com.prod.sellBox.dto.RoomDto;
import com.prod.sellBox.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public long createRoom(@RequestBody RoomDto room, @AuthenticationPrincipal UserEntity user) {
        log.info("create Room : {}", room.toString());
        RoomInfo newRoom = RoomInfo.builder()
                        .roomName(room.getRoomName())
                        .hostId(user.getUser().getUserId())
                        .thumbnailId(room.getThumbnailId())
                        .build();

        return roomService.save(newRoom);
    }

    @GetMapping
    public List<RoomInfo> roomList() {
        log.info("request roomList");
        return roomService.findAll();
    }

    @GetMapping("/{roomId}")
    public RoomInfo enterRoom(@PathVariable Long roomId) {
        log.info("request enterRoom : {}", roomId);
        return roomService.findById(roomId);
    }

    @PostMapping("/{roomId}")
    public void editRoom(@RequestBody RoomDto newRoom, @PathVariable Long roomId) {
        RoomInfo room = roomService.findById(roomId);
        room.editName(newRoom.getRoomName());
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        log.info("request deleteRoom : {}", roomId);
        roomService.deleteById(roomId);
    }

}
