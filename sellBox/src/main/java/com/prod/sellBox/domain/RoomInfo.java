package com.prod.sellBox.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ROOM")
@Getter
@RequiredArgsConstructor
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class RoomInfo {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private long Id;
    private String roomName;
    private String hostId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_id")
    private Thumbnail thumbnail;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<User> userList;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public RoomInfo(String roomName, String hostId, Thumbnail thumbnail) {
        this.roomName = roomName;
        this.hostId = hostId;
        this.thumbnail = thumbnail;
    }

    public void editName(String roomName) {
        this.roomName = roomName;
    }

}
