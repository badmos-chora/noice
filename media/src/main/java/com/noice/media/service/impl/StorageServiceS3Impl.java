package com.noice.media.service.impl;

import com.noice.media.service.interfaces.StorageService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("!dev")
public class StorageServiceS3Impl implements StorageService {
    @Override
    public void uploadFile(String uniqueFileName, String bucketName, MultipartFile file) {

    }
}
