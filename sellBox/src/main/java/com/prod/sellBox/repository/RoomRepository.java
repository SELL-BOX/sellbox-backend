package com.prod.sellBox.repository;

import com.prod.sellBox.domain.RoomInfo;
import com.prod.sellBox.domain.User;
import com.prod.sellBox.dto.DeleteRoomResult;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RoomRepository {

    @PersistenceContext
    private EntityManager em;

    public long save(RoomInfo room) {
        em.persist(room);

        return room.getId();
    }

    public List<RoomInfo> findAll() {
        return em.createQuery("select r from RoomInfo r",RoomInfo.class).getResultList();
    }

    public RoomInfo findById(Long roomId) {
        return em.createQuery("select r from RoomInfo r where r.Id = :roomId", RoomInfo.class)
                .setParameter("roomId", roomId)
                .getSingleResult();
    }

    public void deleteById(Long roomId) {
        em.remove(findById(roomId));
    }

}
