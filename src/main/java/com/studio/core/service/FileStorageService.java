package com.studio.core.service;

import com.studio.core.service.provider.FileStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileStorageService implements FileStorageProvider {

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload.dir:./uploads}") String uploadDirPath) throws IOException {
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    public String store(MultipartFile file, String subfolder) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Arquivo vazio");
        }

        subfolder = subfolder.replaceAll("[^a-zA-Z0-9_-]", "");

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).replaceAll("[^a-zA-Z0-9.]", "");
        }

        String filename = subfolder + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + extension;

        Path subfolderPath = uploadDir.resolve(subfolder);
        Files.createDirectories(subfolderPath);

        Path targetPath = subfolderPath.resolve(filename).normalize();
        if (!targetPath.startsWith(uploadDir)) {
            throw new IOException("Caminho de arquivo inválido");
        }

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return subfolder + "/" + filename;
    }

    public byte[] load(String filePath) throws IOException {
        Path path = uploadDir.resolve(filePath);
        return Files.readAllBytes(path);
    }

    public void delete(String filePath) throws IOException {
        Path path = uploadDir.resolve(filePath);
        Files.deleteIfExists(path);
    }
}
