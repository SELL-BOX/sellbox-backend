package com.prod.sellBox.controller;

import com.prod.sellBox.domain.RoomInfo;
import com.prod.sellBox.domain.Thumbnail;
import com.prod.sellBox.domain.UserEntity;
import com.prod.sellBox.dto.RoomDto;
import com.prod.sellBox.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public RoomInfo createRoom(@RequestPart RoomDto room,
                               @RequestPart MultipartFile imgFile,
                               @AuthenticationPrincipal UserEntity user) throws IOException {
        log.info("create Room : {}", room.toString());
        RoomInfo newRoom = RoomInfo.builder()
                        .roomName(room.getRoomName())
                        .hostId(user.getUser().getUserId())
                        .thumbnail(new Thumbnail(UUID.randomUUID().toString(), imgFile.getBytes()))
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

    @PatchMapping("/{roomId}")
    public void editRoom(@RequestBody RoomDto newRoom, @PathVariable Long roomId) {
        RoomInfo room = roomService.findById(roomId);
        room.editName(newRoom.getRoomName());
    }

    @DeleteMapping("/{roomId}/delete")
    public void deleteRoom(@PathVariable Long roomId) {
        log.info("request deleteRoom : {}", roomId);
        roomService.deleteById(roomId);
    }
}
