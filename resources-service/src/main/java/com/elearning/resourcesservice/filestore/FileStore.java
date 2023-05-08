package com.elearning.resourcesservice.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 s3;
    public FileStore(AmazonS3 s3){
        this.s3 = s3;
    }

    public PutObjectResult save(String path, String fileName, Optional<Map<String, String >> optionalMetaData, InputStream inputStream){

        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if(!map.isEmpty()) map.forEach(metadata::addUserMetadata);
        });
        try{
            PutObjectResult putObjectResult = s3.putObject(path, fileName, inputStream, metadata);
            return putObjectResult;
        }catch (AmazonServiceException e){
            throw new IllegalArgumentException("Failed to store file into the s3", e);
        }
    }

    public byte[] download(String key, String path) {
        System.out.println(key);
        System.out.println(path+"SSSSSSSSsssss");
        try{
            S3Object s3Object =  s3.getObject(key,path);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String bucketname, String filename) {
            s3.deleteObject(bucketname,filename);
    }
}
