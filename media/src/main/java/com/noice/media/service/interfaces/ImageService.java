package com.noice.media.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Long saveImage(MultipartFile image);
}
