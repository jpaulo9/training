package com.java.spring.controllers;


import com.java.spring.model.UploadFileResponse;
import com.java.spring.services.FIleStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Tag(name = "File Endpoint")
@RequestMapping("/api/file/v1")
@RestController
public class FIleController {

    private Logger logger = Logger.getLogger(FIleController.class.getName());

    @Autowired
    private FIleStorageService fIleStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file){

        logger.info("Storage file to Disk");

        var fileName = fIleStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/v1/downloadFile/")
                .path(fileName)
                .toUriString();
        return new UploadFileResponse(
                fileName, fileDownloadUri, file.getContentType(), file.getSize()
        );

    }
    @PostMapping("/uploadMultFiles")
    public List<UploadFileResponse> uploadMultFile(@RequestParam("files") MultipartFile[] files){

        logger.info("Storage file to Disk");


        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());

    }

    @GetMapping("/downloadFile/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request){

        logger.info("Reading a file to Disk");

        Resource resource = fIleStorageService.loadFileAsResource(filename);

        String contentype = "";

        try{

            contentype = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        }catch (Exception ex){
            logger.info("Could not determine file type");
        }


        if (contentype.isBlank()) contentype = "application/octet-stream";


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentype))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+ resource.getFilename() +"\"")
                .body(resource);

    }
}
