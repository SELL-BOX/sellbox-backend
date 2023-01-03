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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public void save(UserDto userDto) {
        User newUser = User.builder()
                .userId(userDto.getUserId())
                .userPw(userDto.getUserPw())
                .email(userDto.getEmail())
                .role(setRole(userDto.getRole()))
                .build();

        userRepository.save(newUser);
    }

    public User login(LoginDto loginDto) {
        return userRepository.findByUserId(loginDto.getUserId()).get();
    }

    public User findByUserId(String userId) {
        if (userRepository.existsByUserId(userId)) {
            return userRepository.findByUserId(userId).get();
        }

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = null;
        if (userRepository.findByUserId(userId).isEmpty()) {
            throw new UsernameNotFoundException(userId);
        } else {
            user = userRepository.findByUserId(userId).get();
        }

        return new UserEntity(user);
    }


    private UserRole setRole(String role) {
        if (role.equals("presenter")) {
            return UserRole.ROLE_PRESENTER;
        } else {
            return UserRole.ROLE_VIEWER;
        }
    }
}
