package com.ecom.spring_boot_ecom.serviceImpl;

import com.ecom.spring_boot_ecom.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    public String uploadImage(String path, MultipartFile image) throws IOException {
        // filename of current / original file
        String originalFileName = image.getOriginalFilename();
        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        // mat.jpg -> new file name fill be 1234.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;


        // check if path exits and create
        File filder = new File(path);
        if(!filder.exists()){
            filder.mkdir();
        }
        //upload to server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        // return file name
        return fileName;
    }
}
