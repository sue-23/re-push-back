//package com.example.backend.service.objectstorage;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.example.backend.config.NaverConfig;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.InputStream;
//import java.util.UUID;
//
//@Service
//public class ObjectStorageServiceImpl implements ObjectStorageService {
//
//
//
//    final AmazonS3 s3;
//
//    public ObjectStorageServiceImpl(NaverConfig naverConfig) {
//        s3 = AmazonS3ClientBuilder.standard()
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
//                        naverConfig.getEndPoint(), naverConfig.getRegionName()))
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
//                        naverConfig.getAccessKey(), naverConfig.getSecretKey())))
//                .build();
//    }
//
//    @Override
//    public String uploadFile(String bucketName, String directoryPath, MultipartFile file) {
//        if (file.isEmpty()) {
//            return null;
//        }
//
//        try (InputStream fileIn = file.getInputStream()) {
//            String filename = UUID.randomUUID().toString();
//
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentType(file.getContentType());
//
//            PutObjectRequest objectRequest = new PutObjectRequest(
//                    bucketName,
//                    directoryPath + filename,
//                    fileIn,
//                    objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
//
//            s3.putObject(objectRequest);
//
//            return filename;
//
//        } catch (Exception e) {
//            throw new RuntimeException("파일 업로드 오류", e);
//        }
//    }
//}