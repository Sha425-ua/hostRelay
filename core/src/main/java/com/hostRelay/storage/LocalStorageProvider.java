package com.hostRelay.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class LocalStorageProvider implements StorageProvider {
    private final Path rootDir;

    public LocalStorageProvider(Path rootDir) {
        this.rootDir = rootDir.toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            throw new StorageException(e.getMessage(), e.getCause());
        }
    }

    private Path resolveKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Storage key must not be null or blank");
        }

        Path result = rootDir.resolve(key).normalize();
        if (!result.startsWith(rootDir)) {
            throw new IllegalArgumentException("Invalid key: " + key);
        }
        return result;
    }

    @Override
    public void uploadFile(String key, Path source) {
        if (source == null || !Files.isRegularFile(source)) {
            throw new StorageException("Invalid source: " + source);
        }

        Path target = resolveKey(key);

        try {
            if (target.getParent() != null) {
                Files.createDirectories(target.getParent());
            }
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to upload file: " + key, e);
        }
    }

    @Override
    public void downloadFile(String key, Path target) {
        if (target == null) {
            throw new IllegalArgumentException("Target path must not be null");
        }

        Path source = resolveKey(key);

        if (!Files.isRegularFile(source)) {
            throw new StorageException("Invalid source: " + source);
        }

        try {
            if (target.getParent() != null) {
                Files.createDirectories(target.getParent());
            }
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to download file: " + key, e);
        }
    }

    @Override
    public boolean exists(String key) {
        return Files.isRegularFile(resolveKey(key));
    }

    @Override
    public boolean delete(String key) {
        Path target = resolveKey(key);
        try {
            return Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file: " + key, e);
        }
    }

    @Override
    public void writeBytes(String key, byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null");
        }

        Path target = resolveKey(key);

        try {
            if (target.getParent() != null) {
                Files.createDirectories(target.getParent());
            }
            Files.write(target, data);
        } catch (IOException e) {
            throw new StorageException("Failed to write file: " + key, e);
        }
    }

    @Override
    public byte[] readBytes(String key) {
        Path target = resolveKey(key);
        try {
            if (!Files.isRegularFile(target)) {
                throw new StorageException("File does not exist: " + key);
            }
            return Files.readAllBytes(target);
        } catch (IOException e) {
            throw new StorageException("Failed to read file: " + key, e);
        }
    }

    @Override
    public List<String> list(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix must not be null");
        }

        try (var paths = Files.walk(rootDir)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> rootDir.relativize(path).toString().replace('\\', '/'))
                    .filter(key -> key.startsWith(prefix))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            throw new StorageException("Failed to list files: " + prefix, e);
        }
    }
}
