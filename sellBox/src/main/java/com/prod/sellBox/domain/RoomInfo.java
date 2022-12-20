package com.prod.sellBox.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomInfo {
    int Id;
    String roomId;
    String roomName;
    String hostId;


    @Builder
    public RoomInfo(int id, String roomId, String roomName, String hostId) {
        Id = id;
        this.roomId = roomId;
        this.roomName = roomName;
        this.hostId = hostId;
    }
}
