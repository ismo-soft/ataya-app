package com.ataya.company.controller;

import com.ataya.company.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company/image")
public class ImageController {

    private final FileService fileService;

    public ImageController(FileService fileService) {
        this.fileService = fileService;
    }


    @GetMapping()
    public ResponseEntity<byte[]> getImage(@RequestParam("in") String image) {
        // replace all _ with /
        // image = image.replace("_", "/");
        byte[] imageData = fileService.getImage(image);
        String fileExtension = image.substring(image.lastIndexOf(".") + 1);
        MediaType mediaType;

        switch (fileExtension.toLowerCase()) {
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            case "jpeg":
            case "jpg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case "gif":
                mediaType = MediaType.IMAGE_GIF;
                break;
            case "webp":
                mediaType = MediaType.parseMediaType("image/webp");
                break;
            default:
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(imageData);
    }
}
