package com.ataya.company.exception.custom;

import com.ataya.company.enums.ErrorCode;

import java.util.Map;

public class FileStorageException extends BaseException{

    private final String fileName;
    private final String fileType;
    private final String fileSize;
    private final String fileExtension;

    public FileStorageException(String fileName, String fileType, String fileSize, String fileExtension) {
        super(
                ErrorCode.File_STORAGE_ERROR,
                "File storage error",
                Map.of("fileName", fileName, "fileType", fileType, "fileSize", fileSize, "fileExtension", fileExtension)
        );
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }

    public FileStorageException(String fileName, String fileType, String fileSize, String fileExtension, String errorDescription) {
        super(
                ErrorCode.File_STORAGE_ERROR,
                errorDescription,
                Map.of("fileName", fileName, "fileType", fileType, "fileSize", fileSize, "fileExtension", fileExtension)
        );
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }

}
