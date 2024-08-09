package org.shooong.push.global.objectstorage;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageService {
    String uploadFile(String bucketName, String directoryPath, MultipartFile file);
}
