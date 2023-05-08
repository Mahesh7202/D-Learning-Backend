package com.elearning.resourcesservice.config.amazon;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {

    @Bean
    public AmazonS3 s3(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(
                "AKIAXUCIOAT7RO32QJAQ",
                "qVjTFCN2FNhEc3PlcKdzyj4ziMksJv65Aj1wyFC7"
        );

        return AmazonS3ClientBuilder.standard().withRegion("ap-northeast-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

}
