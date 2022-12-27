package com.prod.sellBox.repository;

import com.prod.sellBox.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findByUserId(String userId) {
        return em.createQuery("select m from User m where m.userId = :userId", User.class)
                .setParameter("userId", userId)
                .getSingleResult();

    }

}


