package com.prod.sellBox.repository;

import com.prod.sellBox.domain.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomInfo, Long> {

}
