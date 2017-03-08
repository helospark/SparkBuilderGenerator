package com.helospark.sparktemplatingplugin.initializer.examplescript;

import java.util.List;
import java.util.Optional;

import com.helospark.sparktemplatingplugin.Activator;
import com.helospark.sparktemplatingplugin.initializer.examplescript.domain.ExampleScriptDescriptorDomain;
import com.helospark.sparktemplatingplugin.initializer.examplescript.xml.ExampleScriptXmlLoader;
import com.helospark.sparktemplatingplugin.preferences.PreferenceStore;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;

public class ExampleScriptInitializer {
    public static final String EXAMPLE_SCRIPT_PREFERENCE_BASE_PATH = Activator.EDITOR_ID + ".InitializedExampleScripts";
    private ExampleScriptXmlLoader exampleScriptXmlLoader;
    private ScriptRepository scriptRepository;
    private UniqueCommandNameFinder uniqueCommandNameFinder;
    private PreferenceStore preferenceStore;

    public ExampleScriptInitializer(ExampleScriptXmlLoader exampleScriptXmlLoader, ScriptRepository scriptRepository, UniqueCommandNameFinder uniqueCommandNameFinder,
            PreferenceStore preferenceStore) {
        this.exampleScriptXmlLoader = exampleScriptXmlLoader;
        this.scriptRepository = scriptRepository;
        this.uniqueCommandNameFinder = uniqueCommandNameFinder;
        this.preferenceStore = preferenceStore;
    }

    public void initialize() {
        try {
            List<ExampleScriptDescriptorDomain> loadedScripts = exampleScriptXmlLoader.load();
            loadedScripts.stream()
                    .forEach(script -> addToScriptRepository(script));
        } catch (Exception e) {
            System.out.println("[ERROR] Cannot initialize script library " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addToScriptRepository(ExampleScriptDescriptorDomain script) {
        if (!doesUserAlreadyHasLatestRevision(script)) {
            ScriptEntity newScriptEntity = createScriptEntity(script);
            scriptRepository.saveNewScript(newScriptEntity);

            int currentLatestRevisionIndex = getLatestRevisionIndex(script);
            updatePreferenceStoreWithNewSavedScriptName(script.getCommandName(), newScriptEntity.getCommandName(), currentLatestRevisionIndex);
        }
    }

    private boolean doesUserAlreadyHasLatestRevision(ExampleScriptDescriptorDomain script) {
        int latestRevisionIndex = getLatestRevisionIndex(script);
        String preferenceName = composePreferenceName(script.getCommandName(), latestRevisionIndex);
        return preferenceStore.getPreference(preferenceName) != null;
    }

    private int getLatestRevisionIndex(ExampleScriptDescriptorDomain script) {
        return script.getRevisions().size() - 1;
    }

    private ScriptEntity getLatestRevision(ExampleScriptDescriptorDomain script) {
        List<ScriptEntity> revision = script.getRevisions();
        return revision.get(getLatestRevisionIndex(script));
    }

    private ScriptEntity createScriptEntity(ExampleScriptDescriptorDomain script) {
        ScriptEntity latestRevision = getLatestRevision(script);
        Optional<ScriptEntity> earlierSavedRevision = getEarlierSavedRevision(script);
        if (earlierSavedRevision.isPresent()) {
            return latestRevision.newWithCommandName(earlierSavedRevision.get().getCommandName());
        } else if (!hasCommandWithCommandName(script.getCommandName())) {
            return latestRevision.newWithCommandName(script.getCommandName());
        } else {
            String uniqueCommandName = uniqueCommandNameFinder.findUniqueCommandName(script.getCommandName());
            return latestRevision.newWithCommandName(uniqueCommandName);
        }
    }

    private void updatePreferenceStoreWithNewSavedScriptName(String originalCommandName, String newCommandName, int latestRevisionIndex) {
        String preferenceName = composePreferenceName(originalCommandName, latestRevisionIndex);
        preferenceStore.setPreference(preferenceName, newCommandName);
    }

    private boolean hasCommandWithCommandName(String commandName) {
        return scriptRepository.loadByCommandName(commandName).isPresent();
    }

    private Optional<ScriptEntity> getEarlierSavedRevision(ExampleScriptDescriptorDomain script) {
        for (int i = 0; i < script.getRevisions().size(); ++i) {
            Optional<ScriptEntity> earlierSavedRevision = getSavedScriptEntityByRevisionIndex(script.getCommandName(), i);
            ScriptEntity currentlyCheckedRevision = script.getRevisions().get(i);
            if (earlierSavedRevision.isPresent() && earlierSavedRevision.get().hasSameContentAs(currentlyCheckedRevision)) {
                return earlierSavedRevision;
            }
        }
        return Optional.empty();
    }

    private Optional<ScriptEntity> getSavedScriptEntityByRevisionIndex(String commandName, int revisionIndex) {
        String name = preferenceStore.getPreference(composePreferenceName(commandName, revisionIndex));
        if (name != null) {
            return scriptRepository.loadByCommandName(name);
        }
        return Optional.empty();
    }

    private String composePreferenceName(String commandName, int latestRevisionIndex) {
        return EXAMPLE_SCRIPT_PREFERENCE_BASE_PATH + "_" + commandName + "_" + latestRevisionIndex;
    }
}
