package com.prod.sellBox.controller;

import com.prod.sellBox.domain.Thumbnail;
import com.prod.sellBox.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/thumbnail")
public class ThumbnailController {

    private final ThumbnailService thumbnailService;

    @GetMapping("/{thumbnailId}")
    public Thumbnail getThumbnail(@PathVariable String thumbnailId) {
        return thumbnailService.getThumbnail(thumbnailId);
    }

    @PutMapping("/{thumbnailId}")
    public void updateThumbnail(@RequestBody Thumbnail thumbnail) {
        thumbnailService.updateThumbnail(thumbnail);
    }

    @DeleteMapping("/{thumbnailId}")
    public void deleteThumbnail(@PathVariable String thumbnailId) {
        thumbnailService.deleteThumbnail(thumbnailId);
    }
}
