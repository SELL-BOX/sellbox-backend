package com.prod.sellBox.repository;

import com.prod.sellBox.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class UserRepository {

    private static final ConcurrentHashMap<String, User> store = new ConcurrentHashMap<>();

    public boolean save(User newUser) {
        if (store.contains(newUser.getUserId())) return false;

        store.put(newUser.getUserId(), newUser);
        return true;
    }

    public User findUserById(String userId) {
        return store.get(userId);
    }

}


