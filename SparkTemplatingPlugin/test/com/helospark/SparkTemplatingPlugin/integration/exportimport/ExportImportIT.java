package com.helospark.SparkTemplatingPlugin.integration.exportimport;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Optional;

import org.eclipse.core.runtime.IProgressMonitor;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.scriptexport.job.ExportJobWorker;
import com.helospark.sparktemplatingplugin.scriptimport.job.ImportJobWorker;
import com.helospark.sparktemplatingplugin.support.fileoperation.FileContentReader;
import com.helospark.sparktemplatingplugin.support.fileoperation.FileOutputWriter;

public class ExportImportIT {
    private ImportJobWorker importJobWorker;
    private ExportJobWorker exportJobWorker;

    private ScriptEntity firstScript = new ScriptEntity("commandName1", "script1", 1);
    private ScriptEntity secondScript = new ScriptEntity("commandName2", "script2", 2);

    @Mock
    private IProgressMonitor progressMonitor;
    @Mock
    private FileContentReader fileContentReader;
    @Mock
    private FileOutputWriter fileOutputWriter;
    @Mock
    private ScriptRepository scriptRepository;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        DiContainer.clearDiContainer();

        // Override real dependencies with mocks
        DiContainer.addDependency(fileContentReader);
        DiContainer.addDependency(fileOutputWriter);
        DiContainer.addDependency(fileContentReader);
        DiContainer.addDependency(scriptRepository);
        // end of overrides

        DiContainer.initializeDiContainer();
        importJobWorker = DiContainer.getDependency(ImportJobWorker.class);
        exportJobWorker = DiContainer.getDependency(ExportJobWorker.class);

    }

    @Test
    public void testExportThenImportShouldLoadSameDataAsExported() {
        // GIVEN that we have some script
        given(scriptRepository.loadAll()).willReturn(Arrays.asList(firstScript, secondScript));

        // WHEN we export these scripts
        exportJobWorker.export("fileName");

        byte[] exportedByteData = extractSavedZipBytes();

        // AND then importing the exported bytes when no commands exists
        given(fileContentReader.loadContent("fileName")).willReturn(exportedByteData);
        given(scriptRepository.loadByCommandName(anyString())).willReturn(Optional.empty());
        importJobWorker.importToScriptRepository("fileName");

        // THEN we get back the same scripts
        verify(scriptRepository).saveNewScript(firstScript);
        verify(scriptRepository).saveNewScript(secondScript);
    }

    private byte[] extractSavedZipBytes() {
        ArgumentCaptor<byte[]> byteDataCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<String> commandNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(fileOutputWriter).saveFile(byteDataCaptor.capture(), commandNameCaptor.capture());
        return byteDataCaptor.getValue();
    }

}
