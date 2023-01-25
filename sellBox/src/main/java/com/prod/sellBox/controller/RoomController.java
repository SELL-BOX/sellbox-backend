package com.prod.sellBox.controller;

import com.prod.sellBox.config.redis.RedisSubscriber;
import com.prod.sellBox.domain.RoomInfo;
import com.prod.sellBox.domain.Thumbnail;
import com.prod.sellBox.domain.UserEntity;
import com.prod.sellBox.dto.RoomDto;
import com.prod.sellBox.service.RoomService;
import com.prod.sellBox.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;
    private final ThumbnailService thumbnailService;

    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    private Map<String, ChannelTopic> topics;


    @PostConstruct
    private void init() {
        topics = new HashMap<>();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public RoomInfo createRoom(@RequestPart RoomDto room,
                               @RequestPart MultipartFile imgFile,
                               @AuthenticationPrincipal UserEntity user) throws IOException {
        log.info("create Room : {}", room.toString());

        String thumbnailId = UUID.randomUUID().toString();

        RoomInfo newRoom = RoomInfo.builder()
                        .roomName(room.getRoomName())
                        .hostId(user.getUser().getUserId())
                        .thumbnailId(thumbnailId)
                        .build();

        Thumbnail thumbnail = thumbnailService.saveThumbnail(thumbnailId, imgFile);

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

        String sRoomId = roomId.toString();

        ChannelTopic topic = topics.get(sRoomId);
        if (topic == null)
            topic = new ChannelTopic(sRoomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(sRoomId, topic);

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

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
