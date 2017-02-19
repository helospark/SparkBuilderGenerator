package com.helospark.sparktemplatingplugin.repository.zip;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;

public class ScriptUnzipper {

    public List<ScriptEntity> extract(String zipFile) {
        List<ScriptEntity> result = new ArrayList<>();
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                result.add(createScriptEntity(zin, entry));
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to extract zip", e);
        }
        return result;
    }

    private ScriptEntity createScriptEntity(ZipInputStream zin, ZipEntry entry) throws IOException {
        byte[] data = readFileContent(zin);
        return ScriptEntity.deserializeFromBytes(data);
    }

    private byte[] readFileContent(ZipInputStream zipStream) throws IOException {
        byte[] buffer = new byte[1024];
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            int count;
            while ((count = zipStream.read(buffer)) != -1) {
                byteStream.write(buffer, 0, count);
            }
            return byteStream.toByteArray();
        }
    }

}
