package com.helospark.sparktemplatingplugin.initializer.examplescript.xml;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.helospark.sparktemplatingplugin.initializer.examplescript.BundleClasspathFileLoader;
import com.helospark.sparktemplatingplugin.initializer.examplescript.domain.ExampleScriptDescriptorDomain;
import com.helospark.sparktemplatingplugin.initializer.examplescript.xml.domain.ExampleScriptXmlDomain;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.support.fileoperation.FileContentReader;

public class ExampleScriptXmlLoader {
    public static final String EXAMPLE_SCRIPT_CLASSPATH_FOLDER_PATH = "/resources/example-scripts";
    public static final String EXAMPLE_SCRIPT_DESCRIPTOR_XML_PATH = EXAMPLE_SCRIPT_CLASSPATH_FOLDER_PATH + "/example-scripts.xml";

    private ExampleScriptXmlParser exampleScriptXmlParser;
    private FileContentReader fileContentReader;
    private BundleClasspathFileLoader bundleClasspathFileLoader;

    public ExampleScriptXmlLoader(ExampleScriptXmlParser exampleScriptXmlParser, FileContentReader fileContentReader, BundleClasspathFileLoader bundleClasspathFileLoader) {
        this.exampleScriptXmlParser = exampleScriptXmlParser;
        this.fileContentReader = fileContentReader;
        this.bundleClasspathFileLoader = bundleClasspathFileLoader;
    }

    public List<ExampleScriptDescriptorDomain> load() {
        List<ExampleScriptXmlDomain> parsedxml = exampleScriptXmlParser.parse(EXAMPLE_SCRIPT_DESCRIPTOR_XML_PATH);
        return parsedxml.stream()
                .map(xmlElement -> createExampleScriptDescriptorDomain(xmlElement))
                .collect(Collectors.toList());
    }

    private ExampleScriptDescriptorDomain createExampleScriptDescriptorDomain(ExampleScriptXmlDomain parsedXmlElement) {
        List<ScriptEntity> loadedScriptEntities = parsedXmlElement.getRevisionFileNames()
                .stream()
                .map(revision -> loadScriptEntity(revision))
                .map(script -> new ScriptEntity(parsedXmlElement.getCommandName(), script.getScript())) // make the command name same as in XML
                .collect(Collectors.toList());
        return new ExampleScriptDescriptorDomain(parsedXmlElement.getCommandName(), loadedScriptEntities);
    }

    private ScriptEntity loadScriptEntity(String revisionFileName) {
        byte[] contents = loadRevisionFileContents(revisionFileName);
        return ScriptEntity.deserializeFromBytes(contents);
    }

    private byte[] loadRevisionFileContents(String revisionFileName) {
        File revisionFile = bundleClasspathFileLoader.loadFileFromClasspath(EXAMPLE_SCRIPT_CLASSPATH_FOLDER_PATH + "/" + revisionFileName);
        return fileContentReader.loadContent(revisionFile);
    }

}
