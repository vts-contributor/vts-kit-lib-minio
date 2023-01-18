package com.viettel.vtskit.minio.utils;

import com.viettel.vtskit.minio.configuration.MinioProperties;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class Validate {


    public static boolean validateType(MultipartFile file, MinioProperties minioProperties){
        Boolean isValidate = false;
        List<String> allowType = minioProperties.getAllowType();
        String contentType = file.getContentType();
        Long size = file.getSize();
        String[] typeConvert = contentType.split("/");
        String type = typeConvert[1];
        for (String o1: allowType
        ) {
            if (o1.equals(type) ) isValidate = true;

        }
        return isValidate;
    }
    public static boolean validateSize(MultipartFile file, MinioProperties minioProperties){
        long sizeFile = minioProperties.getMaxSize() * 1024 * 1024;
        long size = file.getSize();
        return true ? (size - sizeFile > 0) : false;
    }
}
