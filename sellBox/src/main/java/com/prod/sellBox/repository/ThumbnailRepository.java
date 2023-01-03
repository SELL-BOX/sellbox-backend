package com.prod.sellBox.repository;

import com.prod.sellBox.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, String> {
}
