package com.noice.media.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.heif.HeifDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.png.PngDirectory;
import com.noice.media.entity.ImageAsset;
import com.noice.media.exception.NoiceBusinessLogicException;
import com.noice.media.exception.StorageException;
import com.noice.media.repository.ImageAssetRepository;
import com.noice.media.service.interfaces.ImageService;
import com.noice.media.service.interfaces.StorageService;
import com.noice.media.util.FileHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private ImageAssetRepository imageAssetRepository;
    private StorageService storageService;

    private static  final List<String> imageAllowedTypes = List.of("image/jpeg", "image/png", "image/webp");
    private static final long IMAGE_MAX_SIZE = 50 * 1024 * 1024;
    private static final String IMAGE_BUCKET_NAME = "image";


    @Override
    public Long saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) throw new NoiceBusinessLogicException("Image file cannot be empty");
        if(image.getSize() > IMAGE_MAX_SIZE) throw new NoiceBusinessLogicException("Image size "+ image.getSize()+" is larger than " + IMAGE_MAX_SIZE);
        String detectedContentType = FileHelper.getContentType(image);
        if(!imageAllowedTypes.contains(detectedContentType)) throw new NoiceBusinessLogicException("Image type "+ detectedContentType+" is not allowed");
        String uniqueFileName = FileHelper.createUniqueFileName(image.getOriginalFilename(), detectedContentType);

        //upload image logic
        log.debug("file storage logic started for {}", uniqueFileName);
        storageService.uploadFile(uniqueFileName,IMAGE_BUCKET_NAME, image);
        log.debug("file storage logic completed for {}", uniqueFileName);

        //get image dimensions
        Map<String, Integer> imageDimensions = imageDimensions(image);

        ImageAsset.ImageAssetBuilder<?,?> imageAssetBuilder = ImageAsset.builder();
        String bucketAndFileName = IMAGE_BUCKET_NAME.concat(uniqueFileName);

        imageAssetBuilder
                .width(imageDimensions.get("width"))
                .height(imageDimensions.get("height"))
                .bucket(IMAGE_BUCKET_NAME)
                .mimeType(detectedContentType)
                .sizeBytes(image.getSize())
                .storageKey(uniqueFileName)
                .sha256Hex(FileHelper.generateHashSHA256(bucketAndFileName));
       ImageAsset img = imageAssetRepository.save(imageAssetBuilder.build());
       return img.getId();
    }

    private Map<String, Integer> imageDimensions(MultipartFile image) {
        Integer width = null;
        Integer height = null;

        try(InputStream in = image.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(in);
            PngDirectory png = metadata.getFirstDirectoryOfType(PngDirectory.class);
            if(png != null) {
                width = getInt(png,PngDirectory.TAG_IMAGE_WIDTH);
                height = getInt(png,PngDirectory.TAG_IMAGE_HEIGHT);
            }
            if(width == null || height == null) {
                JpegDirectory jpeg = metadata.getFirstDirectoryOfType(JpegDirectory.class);
                if(jpeg != null) {
                    width = coalesce(getInt(jpeg,JpegDirectory.TAG_IMAGE_WIDTH),width);
                    height = coalesce(getInt(jpeg,JpegDirectory.TAG_IMAGE_HEIGHT),height);
                }
            }
            if(width == null || height == null) {
                ExifSubIFDDirectory exifSub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                if(exifSub != null) {
                    width = coalesce(width, getInt(exifSub, ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
                    height = coalesce(height, getInt(exifSub, ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
                }
            }

            ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (ifd0 != null) {
                width = coalesce(width, getInt(ifd0, ExifIFD0Directory.TAG_IMAGE_WIDTH));
                height = coalesce(height, getInt(ifd0, ExifIFD0Directory.TAG_IMAGE_HEIGHT));
            }

            if (width == null || height == null) {
                HeifDirectory heif = metadata.getFirstDirectoryOfType(HeifDirectory.class);
                if (heif != null) {
                    width = coalesce(width, getInt(heif, HeifDirectory.TAG_IMAGE_WIDTH));
                    height = coalesce(height, getInt(heif, HeifDirectory.TAG_IMAGE_HEIGHT));
                }
            }
            assert width != null;
            assert height != null;
        }catch(IOException | ImageProcessingException ex){
            throw new StorageException("Error while processing image file dimensions",ex);
        }

        return Map.of(
                "width", width,
                "height", height
        );
    }

    private static Integer getInt(Directory dir, int tag) {
        return (dir != null && dir.containsTag(tag)) ? dir.getInteger(tag) : null;
    }

    private static Integer coalesce(Integer a, Integer b) {
        return (a != null) ? a : b;
    }
}
