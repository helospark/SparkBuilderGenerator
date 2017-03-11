package com.helospark.sparktemplatingplugin.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;

public class FileSystemBackedScriptRepository implements ScriptRepository {
    private static final PluginLogger LOGGER = new PluginLogger(FileSystemBackedScriptRepository.class);
    private static final String SPARK_TEMPLATING_TOOL_FILE_ENCODING = "UTF-8";
    private EclipseRootFolderProvider eclipseRootFolderProvider;
    private CommandNameToFilenameMapper commandNameToFilenameMapper;

    public FileSystemBackedScriptRepository(EclipseRootFolderProvider eclipseRootFolderProvider,
            CommandNameToFilenameMapper commandNameToFilenameMapper) {
        this.eclipseRootFolderProvider = eclipseRootFolderProvider;
        this.commandNameToFilenameMapper = commandNameToFilenameMapper;
    }

    @Override
    public void saveNewScript(ScriptEntity entity) {
        try {
            File scriptFile = getFileForCommand(entity.getCommandName());
            if (!scriptFile.exists()) {
                scriptFile.createNewFile();
            } else {
                LOGGER.info("File already exists, updating content " + scriptFile);
            }
            try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(scriptFile), SPARK_TEMPLATING_TOOL_FILE_ENCODING))) {
                bufferedWriter.write(entity.getScript());
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot write to new file", e);
        }
    }

    @Override
    public List<ScriptEntity> loadAll() {
        try {
            File rootDirectory = eclipseRootFolderProvider.provideRootDirectory();
            File[] commands = rootDirectory.listFiles();
            return Arrays.stream(commands)
                    .map(file -> createScriptEntityFromFile(file))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Cannot get a listing of commands", e);
        }
    }

    @Override
    public Optional<ScriptEntity> loadByCommandName(String commandName) {
        File scriptFile = getFileForCommand(commandName);
        Optional<ScriptEntity> result = Optional.empty();
        if (scriptFile.exists()) {
            result = Optional.of(createScriptEntityFromFile(scriptFile));
        }
        return result;
    }

    @Override
    public URI getUriForCommand(String commandName) {
        try {
            File scriptFile = getFileForCommand(commandName);
            return scriptFile.toURI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByCommandName(String commandName) {
        File fileForCommand = getFileForCommand(commandName);
        if (fileForCommand.exists()) {
            fileForCommand.delete();
        }
    }

    private ScriptEntity createScriptEntityFromFile(File file) {
        try {
            String data = new String(Files.readAllBytes(file.toPath()), SPARK_TEMPLATING_TOOL_FILE_ENCODING);
            String commandName = commandNameToFilenameMapper.mapToCommandName(file.getName());
            return new ScriptEntity(commandName, data);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read file " + file);
        }
    }

    private File getFileForCommand(String commandName) {
        File rootDirectory = eclipseRootFolderProvider.provideRootDirectory();
        String fileName = commandNameToFilenameMapper.mapToFilename(commandName);
        return new File(rootDirectory, fileName);
    }

}
