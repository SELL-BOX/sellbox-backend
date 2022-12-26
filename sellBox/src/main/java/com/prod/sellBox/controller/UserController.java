package com.prod.sellBox.controller;

import com.prod.sellBox.config.jwt.JwtTokenProvider;
import com.prod.sellBox.domain.User;
import com.prod.sellBox.domain.UserEntity;
import com.prod.sellBox.dto.LoginDto;
import com.prod.sellBox.dto.UserDto;
import com.prod.sellBox.repository.UserRepository;
import com.prod.sellBox.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String signUp(@RequestBody UserDto userDto) { // 회원등록
        log.info("sign up user : {}", userDto.getUserId());
        return userService.signUp(userDto) ? "success" : "fail (이미 존재하는 ID)";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        log.info("Try Login : {}", loginDto.getUserId());

        User user = userService.login(loginDto);

        if (user == null) return "가입되지 않은 사용자 입니다.";

        return jwtTokenProvider.createToken(user.getUserId(), user.getRole().toString());
    }
}
