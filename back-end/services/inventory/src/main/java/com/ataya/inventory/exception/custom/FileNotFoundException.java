package com.ataya.inventory.exception.custom;

import com.ataya.inventory.enums.ErrorCode;

import java.util.Map;

public class FileNotFoundException extends BaseException{

    private final String fileName;
    private final String fileType;
    private final String fileExtension;

    public FileNotFoundException(String fileName, String fileType, String fileExtension) {
        super(
                ErrorCode.FILE_NOT_FOUND,
                "File not found",
                Map.of("fileName", fileName, "fileType", fileType, "fileExtension", fileExtension)
        );
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileExtension = fileExtension;
    }

    public FileNotFoundException(String fileName, String fileType, String fileExtension, String errorDescription) {
        super(
                ErrorCode.FILE_NOT_FOUND,
                errorDescription,
                Map.of("fileName", fileName, "fileType", fileType, "fileExtension", fileExtension)
        );
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileExtension = fileExtension;
    }


}
