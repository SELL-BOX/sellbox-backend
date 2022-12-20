package com.prod.sellBox.controller;

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

    @PostMapping
    public String signUp(@RequestBody UserDto userDto) { // 회원등록
        log.info("sign up user : {}", userDto.getUserId());
        return userService.signUp(userDto) ? "success" : "fail (이미 존재하는 ID)";
    }

}
