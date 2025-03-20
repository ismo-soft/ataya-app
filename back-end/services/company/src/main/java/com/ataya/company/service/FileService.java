package com.ataya.company.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String saveImageFile(MultipartFile mediaFile, String of, String category, String name);

    byte[] getImage(String imageName);

    List<String> saveImageFiles(List<MultipartFile> images, String product, String companyId, String id);
}
