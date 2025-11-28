package com.paparazzi.antifakenews.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path root;

    public FileStorageService(@Value("${app.uploads.dir}") String dir) throws IOException {
        this.root = Paths.get(dir).toAbsolutePath();
        Files.createDirectories(root);
    }

    public String store(MultipartFile file) throws IOException {
        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) ext = original.substring(original.lastIndexOf('.'));
        String name = UUID.randomUUID().toString().replace("-", "") + ext;
        Path target = root.resolve(name);
        Files.write(target, file.getBytes());
        return "/uploads/" + name;
    }
}
