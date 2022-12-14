package com.prod.sellBox.repository;

import com.prod.sellBox.domain.RoomInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RoomRepository {

    private static final ConcurrentHashMap<String, RoomInfo> store = new ConcurrentHashMap<>();

    public RoomInfo save(RoomInfo room) {
        store.put(room.getRoomId(), room);
        return room;
    }

    public List<RoomInfo> findAll() {
        return new ArrayList<>(store.values());
    }

    public RoomInfo findById(String roomId) {
        return store.get(roomId);
    }

    public void clearStore() {
        store.clear();
    }
}
