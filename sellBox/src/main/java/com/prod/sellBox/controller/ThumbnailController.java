package com.prod.sellBox.controller;

import com.prod.sellBox.domain.Thumbnail;
import com.prod.sellBox.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/thumbnail")
public class ThumbnailController {

    private final ThumbnailService thumbnailService;

    @GetMapping("/{thumbnailId}")
    public byte[] getThumbnail(@PathVariable String thumbnailId) {
        return thumbnailService.getThumbnail(thumbnailId).getImageSource();
    }

    @PutMapping(value = "/{thumbnailId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateThumbnail(@RequestPart Thumbnail thumbnail,
                                @RequestPart MultipartFile imgFile) throws IOException {
        thumbnailService.updateThumbnail(thumbnail, imgFile);
    }

    @DeleteMapping("/{thumbnailId}")
    public void deleteThumbnail(@PathVariable String thumbnailId) {
        thumbnailService.deleteThumbnail(thumbnailId);
    }
}
