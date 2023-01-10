package com.prod.sellBox.controller;

import com.prod.sellBox.config.jwt.JwtTokenProvider;
import com.prod.sellBox.domain.User;
import com.prod.sellBox.dto.LoginDto;
import com.prod.sellBox.dto.UserDto;
import com.prod.sellBox.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public void signUp(@RequestBody UserDto userDto) { // 회원등록
        log.info("sign up user : {}", userDto.getUserId());
        if (userService.findByUserId(userDto.getUserId()) == null) {
            userService.save(userDto);
        } else {
            throw new IllegalStateException("이미 존재하는 사용자입니다.");
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        log.info("Try Login : {}", loginDto.getUserId());

        User user = userService.login(loginDto);
        if (user == null) {
            throw new IllegalArgumentException("존재하지않는 유저입니다.");
        }
        
        if (!user.getUserPw().equals(loginDto.getUserPw())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return jwtTokenProvider.createToken(user.getUserId(), user.getRole().toString());
    }
}
