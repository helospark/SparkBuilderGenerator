package com.helospark.sparktemplatingplugin.scriptimport.job;

import java.util.ArrayList;
import java.util.List;

import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptUnzipper;
import com.helospark.sparktemplatingplugin.support.FileContentLoader;

public class ImportJobWorker {
    private ScriptRepository scriptRepository;
    private ScriptUnzipper scriptUnzipper;
    private FileContentLoader fileContentLoader;

    public ImportJobWorker(ScriptRepository scriptRepository, ScriptUnzipper scriptUnzipper, FileContentLoader fileContentLoader) {
        this.scriptRepository = scriptRepository;
        this.scriptUnzipper = scriptUnzipper;
        this.fileContentLoader = fileContentLoader;
    }

    public ImportJobResult importToScriptRepository(String zipFileName) {
        List<String> skippedCommands = new ArrayList<>();
        byte[] loadedData = fileContentLoader.loadContent(zipFileName);
        List<ScriptEntity> scriptEntities = scriptUnzipper.extract(loadedData);
        for (ScriptEntity scriptEntity : scriptEntities) {
            if (scriptRepository.loadByCommandName(scriptEntity.getCommandName()).isPresent()) {
                skippedCommands.add(scriptEntity.getCommandName());
            } else {
                scriptRepository.saveNewScript(scriptEntity);
            }
        }
        return new ImportJobResult(skippedCommands);
    }
}
