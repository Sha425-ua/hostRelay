package com.hostRelay.storage;

import java.nio.file.Path;
import java.util.List;

public interface StorageProvider {
    void uploadFile(String key, Path source);
    void downloadFile(String key, Path target);

    boolean exists(String key);
    boolean delete(String key);
    void writeBytes(String key, byte[] bytes);
    byte[] readBytes(String key);
    List<String> list(String prefix);
}
