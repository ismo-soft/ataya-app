package com.ataya.company.service.impl;

import com.ataya.company.exception.custom.FileNotFoundException;
import com.ataya.company.exception.custom.FileStorageException;
import com.ataya.company.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {

    @Value("${ataya.app.company-service.media.upload.dir}")
    private String uploadDir;

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );
    @Override
    public String saveImageFile(MultipartFile mediaFile, String of, String category, String name) {
        try {
            if (mediaFile.isEmpty()) {
                return "";
            }
            // get file extension
            String fileExtension = Objects.requireNonNull(mediaFile.getOriginalFilename()).substring(mediaFile.getOriginalFilename().lastIndexOf("."));
            String contentType = mediaFile.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
                throw new FileStorageException(
                        name, category, mediaFile.getSize() + " bytes",fileExtension , "Invalid file type"
                );
            }
            Path filePath = Paths.get(uploadDir + "/" + of + "/" + category + "/" + name + fileExtension);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, mediaFile.getBytes());
            // return without the uploadDir
            String path = filePath.toString().substring(uploadDir.length());
            return path.replace("\\", "/");
        } catch (Exception e) {
            throw new FileStorageException(
                    name, category, mediaFile.getSize() + "bytes", "Invalid file type"
            );
        }
    }

    @Override
    public byte[] getImage(String imageName) {
        try {
            Path imagePath = Paths.get(uploadDir + imageName);
            String fileExtension = imageName.substring(imageName.lastIndexOf("."));
            if(!Files.exists(imagePath)) {
                throw new FileNotFoundException(
                        imageName, "image", fileExtension, "Image not found"
                );
            }
            return Files.readAllBytes(imagePath);
        } catch (Exception e) {
            throw new FileStorageException(
                    imageName, "image", "0 bytes", "Invalid file type"
            );
        }
    }

    @Override
    public List<String> saveImageFiles(List<MultipartFile> images, String product, String companyId, String id) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            imageUrls.add(saveImageFile(image, product, companyId, id + "-" + images.indexOf(image)));
        }
        return imageUrls;
    }
}
