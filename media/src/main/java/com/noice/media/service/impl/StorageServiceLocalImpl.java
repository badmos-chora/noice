package com.noice.media.service.impl;

import com.noice.media.exception.StorageException;
import com.noice.media.service.interfaces.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
@Profile("dev")
public class StorageServiceLocalImpl implements StorageService {
    private final  Path ASSET_PATH;

    public StorageServiceLocalImpl(@Value("${storage.asset.path:./assets}") String assetPath) {
        this.ASSET_PATH = Paths.get(assetPath).toAbsolutePath().normalize();
    }

    @Override
    public void uploadFile(String uniqueFileName, String bucketName, MultipartFile file) {
        Path tmp = null;
        try {

            String safeName = Paths.get(uniqueFileName).getFileName().toString();
            Path bucketPath = ASSET_PATH.resolve(bucketName).normalize();
            Path finalPath  = bucketPath.resolve(safeName).normalize();

            if (!finalPath.startsWith(ASSET_PATH)) {
                throw new SecurityException("Path escapes asset base");
            }
            Files.createDirectories(bucketPath);

            Path baseReal   = ASSET_PATH.toRealPath(LinkOption.NOFOLLOW_LINKS);
            Path bucketReal = bucketPath.toRealPath(LinkOption.NOFOLLOW_LINKS);
            if (!bucketReal.startsWith(baseReal)) {
                throw new SecurityException("Bucket path escapes via symlink");
            }
            tmp = Files.createTempFile(bucketPath, ".upload-", ".tmp");
            try {
                try {
                    file.transferTo(tmp);
                } catch (UnsupportedOperationException | IllegalStateException | IOException ex) {
                    try (java.io.InputStream in = file.getInputStream()) {
                        Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                try {
                    Files.move(tmp, finalPath, StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException e) {
                    Files.move(tmp, finalPath);
                }
                tmp = null;
            } finally {
                if (tmp != null) {
                    try { Files.deleteIfExists(tmp); } catch (IOException ignore) {}
                }
            }

        } catch (FileAlreadyExistsException e) {
            throw new StorageException("File already exists: " + uniqueFileName, e);
        } catch (IOException e) {
            throw new StorageException("Failed to upload file", e);
        }
    }

}
