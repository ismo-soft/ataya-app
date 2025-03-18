package com.ataya.company.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveImageFile(MultipartFile mediaFile, String of, String category, String name);

    byte[] getImage(String imageName);
}
