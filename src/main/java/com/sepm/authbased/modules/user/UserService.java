package com.sepm.authbased.modules.user;

import com.sepm.authbased.cores.s3.S3Buckets;
import com.sepm.authbased.cores.s3.S3Service;
import com.sepm.authbased.util.FuncHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final S3Buckets s3Buckets;
    private final S3Service s3Service;

    public UserService(@Qualifier("jpa") UserDAO userDAO, UserDTOMapper userDTOMapper, PasswordEncoder passwordEncoder, S3Buckets s3Buckets, S3Service s3Service) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.s3Buckets = s3Buckets;
        this.s3Service = s3Service;
    }

    public void uploadUserPhotoToS3(Integer customerId, MultipartFile file) {
        String profileImageId = UUID.randomUUID().toString();
        System.out.println(s3Buckets.getCustomer());

        FuncHelper.isFileEmpty(file);
        FuncHelper.isImage(file);

        Map<String, String> metadata = extractMetadata(file);

        String extension = metadata.get("Content-Type").split("/")[1];

        System.out.println(extension);

        String key = String.format("profile-images/%s/%s", customerId, file.getOriginalFilename());

        try{
            s3Service.putObject(s3Buckets.getCustomer(), key, Optional.of(metadata), file.getBytes());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();

        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        return metadata;
    }
}
