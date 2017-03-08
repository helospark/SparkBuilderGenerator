package com.helospark.sparktemplatingplugin.initializer.examplescript;

import java.util.Optional;

import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;

public class UniqueCommandNameFinder {
    private ScriptRepository scriptRepository;

    public UniqueCommandNameFinder(ScriptRepository scriptRepository) {
        this.scriptRepository = scriptRepository;
    }

    public String findUniqueCommandName(String baseCommandName) {
        int i = 1;
        Optional<ScriptEntity> command;
        String lastCommandName = "";
        do {
            lastCommandName = baseCommandName + "_" + i;
            command = scriptRepository.loadByCommandName(lastCommandName);
            ++i;
        } while (command.isPresent());
        return lastCommandName;
    }
}
