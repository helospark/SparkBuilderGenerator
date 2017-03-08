package com.helospark.sparktemplatingplugin.scriptexport.job;

import java.util.List;

import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptZipper;
import com.helospark.sparktemplatingplugin.support.FileOutputWriter;

public class ExportJobWorker {
    private ScriptRepository scriptRepository;
    private ScriptZipper scriptZipper;
    private FileOutputWriter fileOutputWriter;

    public ExportJobWorker(ScriptRepository scriptRepository, ScriptZipper scriptZipper, FileOutputWriter fileOutputWriter) {
        this.scriptRepository = scriptRepository;
        this.scriptZipper = scriptZipper;
        this.fileOutputWriter = fileOutputWriter;
    }

    public void export(String fileName) {
        List<ScriptEntity> export = scriptRepository.loadAll();
        byte[] zipData = scriptZipper.createZip(export);
        fileOutputWriter.saveFile(zipData, fileName);
    }
}
