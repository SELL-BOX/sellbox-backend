package com.prod.sellBox.domain;


import com.prod.sellBox.dto.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
@Getter
@RequiredArgsConstructor
public class User {

    @Id @GeneratedValue
    private long id;
    private String userId;
    private String userPw;
    private String email;
    @Enumerated
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomInfo room;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public User(String userId, String userPw, String email, UserRole role) {
        this.userId = userId;
        this.userPw = userPw;
        this.email = email;
        this.role = role;
    }
}
