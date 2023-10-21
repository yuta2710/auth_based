package com.sepm.authbased.util;

import org.apache.http.entity.ContentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FuncHelper {
    public static void isImage(MultipartFile file) {
        if(!Arrays.asList(
                ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image");
        }
    }

    public static void isFileEmpty(MultipartFile file) {
        if(file.isEmpty()) {
           throw new IllegalStateException("File must not be empty");
        }
    }


}
