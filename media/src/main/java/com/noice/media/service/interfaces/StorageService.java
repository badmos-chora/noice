package com.noice.media.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void uploadFile(String uniqueFileName, String bucketName, MultipartFile file);
}
