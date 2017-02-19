package com.helospark.sparktemplatingplugin.repository.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.helospark.sparktemplatingplugin.repository.CommandNameToFilenameMapper;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;

/**
 * Adapted from:
 * http://www.avajava.com/tutorials/lessons/how-do-i-zip-a-directory-and-all-its-contents.html.
 * 
 * @author helospark
 */
public class ScriptZipper {
    private CommandNameToFilenameMapper commandNameToFilenameMapper;

    public ScriptZipper(CommandNameToFilenameMapper commandNameToFilenameMapper) {
        this.commandNameToFilenameMapper = commandNameToFilenameMapper;
    }

    public byte[] createZip(List<ScriptEntity> scriptsToSave) {
        return writeZipFile(scriptsToSave);
    }

    public byte[] writeZipFile(List<ScriptEntity> scriptsToSave) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] result;
        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {

            scriptsToSave.stream().forEach(scriptEntity -> addToZip(scriptEntity, zos));

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            result = outputStream.toByteArray();
            closeStream(outputStream);
        }
        return result;
    }

    private void closeStream(ByteArrayOutputStream outputStream) {
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToZip(ScriptEntity scriptEntity, ZipOutputStream zipOutputStream) {
        try {
            String zipFilePath = commandNameToFilenameMapper.mapToFilename(scriptEntity.getCommandName());

            ZipEntry zipEntry = new ZipEntry(zipFilePath);
            zipOutputStream.putNextEntry(zipEntry);

            zipOutputStream.write(scriptEntity.serializeToBytes());

            zipOutputStream.closeEntry();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getOutputFormat() {
        return "zip";
    }
}
