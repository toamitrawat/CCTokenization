package com.example.tokenization.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;

@Service
public class S3UploadService {
    private static final Logger logger = LogManager.getLogger(S3UploadService.class);
    private final S3Client s3Client;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3UploadService() {
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1)
                // .endpointOverride(URI.create("https://s3.ap-south-1.amazonaws.com")) // Optional
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public String uploadFile(MultipartFile file, String filename, String uploadUser) throws IOException {
        String key = uploadUser + "/" + filename;
        logger.info("Uploading file to S3: user={}, filename={}, key={}", uploadUser, filename, key);
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
            logger.info("File uploaded successfully: key={}", key);
            return key;
        } catch (Exception e) {
            logger.error("Failed to upload file to S3: user={}, filename={}, key={}, error={}", uploadUser, filename, key, e.getMessage(), e);
            throw e;
        }
    }
}
