package com.java.spring.services;


import com.java.spring.config.FileStorageConfig;
import com.java.spring.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {

        Path path = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileStorageLocation = path;

        try{

            Files.createDirectories(this.fileStorageLocation);

        }catch (Exception ex){
                throw new FileStorageException("Não foi possível definir o diretório para criar os arquivos!",ex);
        }
    }


    public String storeFile (MultipartFile file ){
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try{

            if(filename.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence "+filename);
            }

            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        }catch (Exception ex){
            throw new FileStorageException(
                    "Could Not store file "+filename+". Please  try again!", ex

            );
        }
    }

    public Resource loadFileAsResource(String fileName){
        try{

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource( filePath.toUri());
            if (resource.exists()) return resource;
            else throw new FileStorageException("File not found");

        }catch (Exception ex){
            throw new FileStorageException("File not found "+fileName, ex);
        }
    }
}
