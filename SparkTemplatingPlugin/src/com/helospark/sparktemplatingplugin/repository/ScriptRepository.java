package com.helospark.sparktemplatingplugin.repository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;

public interface ScriptRepository {

    void saveNewScript(ScriptEntity entity);

    List<ScriptEntity> loadAll();

    Optional<ScriptEntity> loadByCommandName(String commandName);

    URI getUriForCommand(String commandName);

}