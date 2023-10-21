package com.sepm.authbased.modules.user;

import com.sepm.authbased.cores.jwt.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "{customerId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadUserPhotoToS3(@PathVariable("customerId") Integer customerId, @RequestParam("file") MultipartFile file) {
        System.out.println(customerId);
        userService.uploadUserPhotoToS3(customerId, file);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Upload image to S3 bucket successfully");

        return ResponseEntity.ok(response);
    }
}
