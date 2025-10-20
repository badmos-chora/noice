package com.noice.media.util;

import com.noice.media.exception.StorageException;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

public class FileHelper {

    private static final Pattern CONTROL_CHARS_PATTERN = Pattern.compile("[\\p{Cntrl}]");
    private static final Pattern UNSAFE_CHARS_PATTERN = Pattern.compile("[<>:\"|?*\\\\/\\p{C}]");
    private static final Pattern LEADING_TRAILING_PATTERN = Pattern.compile("^[\\.\\s]+|[\\.\\s]+$");
    private static final Pattern MIME_PARAMS_PATTERN = Pattern.compile(";.*$");
    private static final Tika tika = new Tika();

    private static final int UUID_LENGTH = 12;
    private static final int MAX_BASE_NAME_LENGTH = 10;

    /**
     * Creates a unique, sanitized filename safe for local and S3 storage.
     * Format: {baseName}-{uuid}-{timestamp}.{ext}
     * Example: profile_ph-a3b2c1d4-2025-10-09-14.jpg
     *
     * @param fileName Original filename (must not be null)
     * @param contentType MIME type (must not be null, may contain parameters)
     * @return Sanitized unique filename
     */
    public static String createUniqueFileName(String fileName, String contentType) {

        String safeName = FilenameUtils.getName(fileName);
        if (safeName == null || safeName.isEmpty()) {
            safeName = "file";
        }
        safeName = CONTROL_CHARS_PATTERN.matcher(safeName).replaceAll("");
        safeName = UNSAFE_CHARS_PATTERN.matcher(safeName).replaceAll("_");
        safeName = LEADING_TRAILING_PATTERN.matcher(safeName).replaceAll("");

        String uniqueFileName = safeName.isEmpty() ? "file" : FilenameUtils.getBaseName(safeName);

        String cleanContentType = MIME_PARAMS_PATTERN.matcher(contentType).replaceAll("").trim();

        String extension;
        try {
            extension = MimeTypes.getDefaultMimeTypes().forName(cleanContentType).getExtension();
        } catch (MimeTypeException ex) {
            String fileExt = FilenameUtils.getExtension(fileName);
            extension = (fileExt == null || fileExt.isBlank()) ? "" : "." + fileExt.toLowerCase();
        }

        if (uniqueFileName.length() > MAX_BASE_NAME_LENGTH) {
            uniqueFileName = uniqueFileName.substring(0, MAX_BASE_NAME_LENGTH);
        }

        uniqueFileName += "-" + UUID.randomUUID().toString().substring(0, UUID_LENGTH);

        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());

        uniqueFileName += "-" + timestamp + extension;

        return uniqueFileName;
    }
    public static String getContentType(MultipartFile multipartFile) {
        try (InputStream is = multipartFile.getInputStream()) {
            return tika.detect(is, multipartFile.getOriginalFilename());
        } catch (IOException e) {
            return multipartFile.getContentType();
        }
    }

    public static String generateHashSHA256(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((content.getBytes(StandardCharsets.UTF_8)));
            return new String(Hex.encode(hash));
        }catch (Exception e) {
            throw new StorageException("Error while generating SHA-256 hash",e);
        }
    }

}
