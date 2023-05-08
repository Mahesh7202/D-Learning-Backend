package com.elearning.resourcesservice.buckets;

import lombok.Data;


public enum BucketName {

    PROFILE_IMAGE("elearningres");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
