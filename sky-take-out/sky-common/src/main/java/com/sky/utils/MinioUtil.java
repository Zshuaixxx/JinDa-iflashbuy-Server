package com.sky.utils;

import io.minio.*;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MinIO文件上传工具类
 */
@Data
@AllArgsConstructor
@Slf4j
public class MinioUtil {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    /**
     * 文件上传
     *
     * @param bytes 文件字节数组
     * @param objectName 对象名称
     * @return 文件访问URL
     */
    public String upload(byte[] bytes, String objectName) {
        try {
            log.info("开始上传文件到MinIO: endpoint={}, bucket={}, object={}", endpoint, bucketName, objectName);
            
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .build()
            );

            // 构造文件访问路径
            StringBuilder stringBuilder = new StringBuilder(endpoint);
            if (!endpoint.endsWith("/")) {
                stringBuilder.append("/");
            }
            stringBuilder.append(bucketName).append("/").append(objectName);

            String fileUrl = stringBuilder.toString();
            log.info("文件上传成功: {}", fileUrl);
            return fileUrl;
        } catch (Exception e) {
            log.error("文件上传失败: endpoint={}, bucket={}, object={}", endpoint, bucketName, objectName, e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
}