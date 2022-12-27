package com.prod.sellBox.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ROOM")
@Getter
@RequiredArgsConstructor
public class RoomInfo {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private long Id;
    private String roomName;
    private String hostId;
    private String thumbnailId;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<User> userList;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public RoomInfo(String roomName, String hostId) {
        this.roomName = roomName;
        this.hostId = hostId;
    }

}
