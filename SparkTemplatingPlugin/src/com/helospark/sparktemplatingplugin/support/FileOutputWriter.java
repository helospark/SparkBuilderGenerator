package com.helospark.sparktemplatingplugin.support;

import java.io.FileOutputStream;

public class FileOutputWriter {

    public void saveFile(byte[] data, String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            fileOutputStream.write(data);
        } catch (Exception e) {
            throw new RuntimeException("Cannot save file " + fileName, e);
        }
    }
}
