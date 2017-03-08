package com.helospark.sparktemplatingplugin.support;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileContentLoader {

    public byte[] loadContent(File file) {
        assertFileExists(file);
        try {
            return Files.readAllBytes(Paths.get(file.getPath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] loadContent(String fileName) {
        return loadContent(new File(fileName));
    }

    private void assertFileExists(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("File " + file.getAbsolutePath() + " does not exists");
        }
    }
}
