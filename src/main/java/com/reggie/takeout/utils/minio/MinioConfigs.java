package com.reggie.takeout.utils.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author gett
 * @Date 2021/12/8  17:33
 * @Description minio
 */

@Configuration
public class MinioConfigs {

    @Value("${minio.endpoint}")
    private String endPoint;
    @Value("${minio.accesskey}")
    private String accessKey;
    @Value("${minio.secretkey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient(){
        MinioClient build = MinioClient.builder().endpoint(endPoint).credentials(accessKey,secretKey).build();
        return build;
    }
}
