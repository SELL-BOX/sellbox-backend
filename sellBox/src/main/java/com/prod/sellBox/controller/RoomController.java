package com.prod.sellBox.controller;

import com.prod.sellBox.domain.RoomInfo;
import com.prod.sellBox.repository.RoomRepository;
import com.prod.sellBox.vo.RoomVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rooms")
public class RoomController {

    RoomRepository roomRepository = new RoomRepository();

    @PostMapping("/create")
    public RoomInfo createRoom(@RequestBody RoomVo roomInfo) {
        log.info("create Room : {}", roomInfo.toString());

        RoomInfo newRoom = new RoomInfo();
        newRoom.setRoomId(roomInfo.getRoomId());
        roomRepository.save(newRoom);

        return newRoom;
    }

    @GetMapping("")
    public List<RoomInfo> roomList() {
        log.info("request roomList");
        return roomRepository.findAll();
    }

    @GetMapping("/{roomId}")
    public RoomInfo enterRoom(@PathVariable String roomId) {
        log.info("request enterRoom : {}", roomId);
        return roomRepository.findById(roomId);
    }

    @DeleteMapping("/{roomId}/delete")
    public String deleteRoom(@PathVariable String roomId) {
        log.info("request deleteRoom : {}", roomId);

        int result = roomRepository.deleteById(roomId);
        String msg = "";

        if (result == 1) {
            msg = "방을 제거했습니다 roomId : ." + roomId;
        } else {
            msg = "존재하지 않는 방입니다.";
        }

        return msg;
    }
}
