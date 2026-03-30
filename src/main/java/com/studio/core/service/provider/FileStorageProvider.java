package com.studio.core.service.provider;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageProvider {
    String store(MultipartFile file, String subfolder) throws IOException;
    byte[] load(String filePath) throws IOException;
    void delete(String filePath) throws IOException;
}
