package com.prod.sellBox.service;

import com.prod.sellBox.domain.User;
import com.prod.sellBox.domain.UserEntity;
import com.prod.sellBox.dto.LoginDto;
import com.prod.sellBox.dto.UserDto;
import com.prod.sellBox.dto.UserRole;
import com.prod.sellBox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public boolean signUp(UserDto userDto) {
        User newUser = User.builder()
                .userId(userDto.getUserId())
                .userPw(userDto.getUserPw())
                .email(userDto.getEmail())
                .role(UserRole.ROLE_VIEWER)
                .build();

        return userRepository.save(newUser);
    }

    public User login(LoginDto loginDto) {
        return userRepository.findUserById(loginDto.getUserId());
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("loadUserByUsername 진입");

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new UsernameNotFoundException(userId);
        }

        return new UserEntity(user);
    }
}
