package com.viettel.vtskit.minio.configuration;

import com.viettel.vtskit.minio.MinioService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.admin.MinioAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioAutoConfiguration {

    private MinioProperties minioProperties;

    private void createBucketIfNotExisted(MinioClient minioClient){
        try {
            if(!minioProperties.isAutoCreateBucket()){
                return;
            }
            String bucket = minioProperties.getBucket();
            if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public MinioService minioService(){
        return new MinioService();
    }

    @Bean
    public MinioClient minioClient(){
        MinioClient.Builder builder = MinioClient.builder();
        builder.endpoint(minioProperties.getServer());
        builder.credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey());
        MinioClient minioClient = builder.build();
        createBucketIfNotExisted(minioClient);
        return minioClient;
    }

    @Bean
    public MinioAdminClient minioAdminClient(){
        MinioAdminClient.Builder builder = MinioAdminClient.builder();
        builder.endpoint(minioProperties.getServer());
        builder.credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey());
        MinioAdminClient minioAdminClient = builder.build();
        return minioAdminClient;
    }

    @Autowired
    public void setMinioProperties(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }
}
