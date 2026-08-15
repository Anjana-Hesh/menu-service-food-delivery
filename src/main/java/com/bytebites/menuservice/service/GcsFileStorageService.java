package com.bytebites.menuservice.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Profile("gcp")
public class GcsFileStorageService implements FileStorageService {

    @Autowired
    private Storage storage;

    @Value("${gcp.bucket-name}")
    private String bucketName;

    @Value("${app.upload.base-url}")
    private String uploadBaseUrl;

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, filename)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        return uploadBaseUrl + filename;
    }
}
