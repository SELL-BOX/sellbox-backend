package com.prod.sellBox.domain;


import com.prod.sellBox.dto.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {

    String userId;
    String userPw;
    String email;
    UserRole role;

    @Builder
    public User(String userId, String userPw, String email, UserRole role) {
        this.userId = userId;
        this.userPw = userPw;
        this.email = email;
        this.role = role;
    }
}
