package com.helospark.sparktemplatingplugin.repository;

import java.util.List;

import com.helospark.sparktemplatingplugin.repository.domain.ExportedScriptData;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptZipper;

public class ScriptRepositoryExporter {
    private ScriptZipper scriptZipper;
    private ScriptRepository scriptRepository;

    public ExportedScriptData export() {
        List<ScriptEntity> scriptsToExport = scriptRepository.loadAll();

        String format = scriptZipper.getOutputFormat();
        byte[] data = scriptZipper.createZip(scriptsToExport);

        return new ExportedScriptData(format, data);
    }
}
