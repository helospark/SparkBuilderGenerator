package com.helospark.SparkTemplatingPlugin.integration.examplescript;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.initializer.examplescript.ExampleScriptInitializer;
import com.helospark.sparktemplatingplugin.initializer.examplescript.ExampleScriptInitializerVersionFilteringDecorator;
import com.helospark.sparktemplatingplugin.initializer.examplescript.UniqueCommandNameFinder;
import com.helospark.sparktemplatingplugin.initializer.examplescript.domain.ExampleScriptDescriptorDomain;
import com.helospark.sparktemplatingplugin.initializer.examplescript.xml.ExampleScriptXmlLoader;
import com.helospark.sparktemplatingplugin.preferences.PreferenceStore;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.support.BundleVersionProvider;

public class ExampleScriptInitializationIT {
    private ExampleScriptInitializerVersionFilteringDecorator underTest;

    @Mock
    private BundleVersionProvider bundleVersionProvider;
    @Mock
    private PreferenceStore preferenceStore;
    @Mock
    private ScriptRepository scriptRepository;
    @Mock
    private ExampleScriptXmlLoader exampleSciptXmlLoader;
    @Mock
    private UniqueCommandNameFinder uniqueCommandNameFinder;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        DiContainer.clearDiContainer();

        // Override real dependencies with mocks
        DiContainer.addDependency(bundleVersionProvider);
        DiContainer.addDependency(preferenceStore);
        DiContainer.addDependency(scriptRepository);
        DiContainer.addDependency(exampleSciptXmlLoader);
        DiContainer.addDependency(uniqueCommandNameFinder);
        // end of overrides

        DiContainer.initializeDiContainer();
        underTest = DiContainer.getDependency(ExampleScriptInitializerVersionFilteringDecorator.class);

        given(bundleVersionProvider.provideVersion()).willReturn("2.0.0");
        given(preferenceStore.getPreference(ExampleScriptInitializerVersionFilteringDecorator.EXAMPLE_SCRIPTS_INITIALIZED_VERSION_KEY)).willReturn("1.0.0");

        ExampleScriptDescriptorDomain descriptorDomain = createExampleScriptDescriptorDomain("command1", "revision1", "revision2");
        given(exampleSciptXmlLoader.load()).willReturn(Arrays.asList(descriptorDomain));
    }

    @Test
    public void testInitializeShouldNowThrowException() {
        // GIVEN

        // WHEN
        underTest.initialize();

        // THEN should pass
    }

    @Test
    public void testInitializeShouldCreateScriptIfDidNotExistEarlier() {
        // GIVEN
        given(preferenceStore.getPreference(anyString())).willReturn(null);
        given(scriptRepository.loadByCommandName("command1")).willReturn(Optional.empty());

        // WHEN
        underTest.initialize();

        // THEN
        ScriptEntity expectedScriptEntity = new ScriptEntity("command1", "revision2");
        verify(scriptRepository).saveNewScript(expectedScriptEntity);
    }

    @Test
    public void testInitializeShouldNotCreateScriptIfAlreadyCreatedInCurrentVersion() {
        // GIVEN
        given(bundleVersionProvider.provideVersion()).willReturn("1.0.0");
        given(preferenceStore.getPreference(ExampleScriptInitializerVersionFilteringDecorator.EXAMPLE_SCRIPTS_INITIALIZED_VERSION_KEY)).willReturn("1.0.0");

        // WHEN
        underTest.initialize();

        // THEN
        verifyNoMoreInteractions(scriptRepository);
        verifyNoMoreInteractions(exampleSciptXmlLoader);
    }

    @Test
    public void testInitializeShouldNotThrowAnyExceptionIfInternallyExceptionOccurres() {
        // GIVEN
        given(exampleSciptXmlLoader.load()).willThrow(new RuntimeException());

        // WHEN
        underTest.initialize();

        // THEN should not throw
    }

    @Test
    public void testInitializeShouldUpdateEarlierRevisionWhenItWasNotModifiedByUser() {
        // GIVEN
        given(preferenceStore.getPreference(ExampleScriptInitializer.EXAMPLE_SCRIPT_PREFERENCE_BASE_PATH + "_command1_0")).willReturn("command1");
        given(scriptRepository.loadByCommandName("command1")).willReturn(Optional.of(new ScriptEntity("command1", "revision1")));
        given(scriptRepository.loadByCommandName(anyString())).willReturn(Optional.empty());

        // WHEN
        underTest.initialize();

        // THEN
        ScriptEntity expectedScriptEntity = new ScriptEntity("command1", "revision2");
        verify(scriptRepository).saveNewScript(expectedScriptEntity);
    }

    @Test
    public void testInitializeShouldNotCallScriptRepositoryWhenUserHasLatestVersion() {
        // GIVEN
        given(preferenceStore.getPreference(ExampleScriptInitializer.EXAMPLE_SCRIPT_PREFERENCE_BASE_PATH + "_command1_1")).willReturn("command1");
        given(scriptRepository.loadByCommandName("command1")).willReturn(Optional.of(new ScriptEntity("command1", "revision2")));

        // WHEN
        underTest.initialize();

        // THEN
        verifyNoMoreInteractions(scriptRepository);
    }

    @Test
    public void testInitializeShouldCreateScriptWithOriginalNameWhenScriptWasModifiedAndScriptDoesNotExistWithOriginalCommandName() {
        // GIVEN
        given(preferenceStore.getPreference(ExampleScriptInitializer.EXAMPLE_SCRIPT_PREFERENCE_BASE_PATH + "_command1_0")).willReturn("command2");
        given(scriptRepository.loadByCommandName("command2")).willReturn(Optional.of(new ScriptEntity("command2", "revision_modified")));
        given(scriptRepository.loadByCommandName("command1")).willReturn(Optional.empty());

        // WHEN
        underTest.initialize();

        // THEN
        ScriptEntity expectedScriptEntity = new ScriptEntity("command1", "revision2");
        verify(scriptRepository).saveNewScript(expectedScriptEntity);
    }

    @Test
    public void testInitializeShouldUpdateScriptWhenUserHasEarlierNotModifiedRevision() {
        // GIVEN
        given(preferenceStore.getPreference(ExampleScriptInitializer.EXAMPLE_SCRIPT_PREFERENCE_BASE_PATH + "_command1_0")).willReturn("previous_revision_name");
        given(scriptRepository.loadByCommandName("previous_revision_name")).willReturn(Optional.of(new ScriptEntity("previous_revision_name", "revision1")));
        given(scriptRepository.loadByCommandName("command1")).willReturn(Optional.empty());

        // WHEN
        underTest.initialize();

        // THEN
        ScriptEntity expectedScriptEntity = new ScriptEntity("previous_revision_name", "revision2");
        verify(scriptRepository).saveNewScript(expectedScriptEntity);
    }

    @Test
    public void testInitializeShouldCreateScriptWithUniqueNameWhenScriptWasModifiedAndScriptAlreadyExistWithOriginalCommandName() {
        // GIVEN
        given(preferenceStore.getPreference(ExampleScriptInitializer.EXAMPLE_SCRIPT_PREFERENCE_BASE_PATH + "_command1_0")).willReturn("command2");
        given(scriptRepository.loadByCommandName("command2")).willReturn(Optional.of(new ScriptEntity("command2", "revision_modified")));
        given(scriptRepository.loadByCommandName("command1")).willReturn(Optional.of(new ScriptEntity("command1", "revision1_modified")));
        given(uniqueCommandNameFinder.findUniqueCommandName("command1")).willReturn("uniqueName");

        // WHEN
        underTest.initialize();

        // THEN
        ScriptEntity expectedScriptEntity = new ScriptEntity("uniqueName", "revision2");
        verify(scriptRepository).saveNewScript(expectedScriptEntity);
    }

    private ExampleScriptDescriptorDomain createExampleScriptDescriptorDomain(String commandName, String... revisionContents) {
        List<ScriptEntity> scriptEntities = Arrays.stream(revisionContents)
                .map(content -> new ScriptEntity(commandName, content))
                .collect(Collectors.toList());
        return new ExampleScriptDescriptorDomain(commandName, scriptEntities);
    }

}
