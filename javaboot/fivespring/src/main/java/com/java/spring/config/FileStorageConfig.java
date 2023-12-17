package com.java.spring.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;


@ConfigurationProperties(prefix = "file")
@Configuration
public class FileStorageConfig {

    private String uploadDir;


    public FileStorageConfig() {
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileStorageConfig that)) return false;
        return Objects.equals(getUploadDir(), that.getUploadDir());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUploadDir());
    }
}
