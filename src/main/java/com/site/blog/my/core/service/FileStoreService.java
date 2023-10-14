package com.site.blog.my.core.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStoreService {
    void uploadFile(String path, MultipartFile file);

    byte[] downloadFile(String fileName);

    List<byte[]> downloadMultipleFiles(List<String> fileNames);

    void deleteFiles(String fileName);
}
