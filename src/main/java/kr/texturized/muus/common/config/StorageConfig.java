package kr.texturized.muus.common.config;

import kr.texturized.muus.infrastructure.amazon.AmazonS3Storage;
import kr.texturized.muus.common.storage.PostImageStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${cloud.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.storage.endpoint}")
    private String endPoint;

    @Value("${cloud.storage.region.static}")
    private String region;

    @Value("${cloud.storage.bucket-name}")
    private String bucketName;

    @Bean
    public PostImageStorage postImageStorage() {
        return new AmazonS3Storage(accessKey, secretKey, endPoint, region, bucketName);
    }
}
