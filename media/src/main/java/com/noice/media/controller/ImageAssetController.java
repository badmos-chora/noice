package com.noice.media.controller;

import com.noice.media.service.interfaces.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(value = "/image",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@AllArgsConstructor
@Tag(name = "Image Apis", description = "apis related to image assets")
public class ImageAssetController {

    private ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('image.add')")
    @Operation(summary = "upload image")
    public ResponseEntity<?> getImageAssetById(@RequestPart(name = "image") MultipartFile image) {
        return ResponseEntity.ok(imageService.saveImage(image));
    }
}
