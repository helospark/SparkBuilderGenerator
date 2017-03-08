package com.helospark.sparktemplatingplugin.initializer.examplescript.xml;

import java.io.File;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.helospark.sparktemplatingplugin.initializer.examplescript.BundleClasspathFileLoader;
import com.helospark.sparktemplatingplugin.initializer.examplescript.xml.domain.ExampleScriptXmlDomain;

public class ExampleScriptXmlParser {
    private XmlDocumentParser xmlDocumentParser;
    private BundleClasspathFileLoader bundleClasspathFileLoader;

    public ExampleScriptXmlParser(XmlDocumentParser xmlDocumentParser, BundleClasspathFileLoader bundleClasspathFileLoader) {
        this.xmlDocumentParser = xmlDocumentParser;
        this.bundleClasspathFileLoader = bundleClasspathFileLoader;
    }

    public List<ExampleScriptXmlDomain> parse(String fileName) {
        List<ExampleScriptXmlDomain> result = new ArrayList<>();
        try {
            File xmlFile = bundleClasspathFileLoader.loadFileFromClasspath(fileName);
            Document document = xmlDocumentParser.parse(xmlFile);
            NodeList commandNodes = document.getElementsByTagName("command");

            for (int i = 0; i < commandNodes.getLength(); i++) {
                Element commandElement = (Element) commandNodes.item(i);
                String commandName = readRequiredAttribute(commandElement, "name");
                List<String> revisionFileNames = parseCommandElement(commandElement);
                result.add(new ExampleScriptXmlDomain(commandName, revisionFileNames));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private List<String> parseCommandElement(Element commandElement) {
        NodeList childNodes = commandElement.getElementsByTagName("revision");

        Map<Integer, String> revisionToFilename = new HashMap<>();

        for (int temp = 0; temp < childNodes.getLength(); temp++) {
            Entry<Integer, String> entry = parseRevision((Element) childNodes.item(temp));
            revisionToFilename.put(entry.getKey(), entry.getValue());
        }
        return revisionToFilename.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());
    }

    private SimpleEntry<Integer, String> parseRevision(Element revision) {
        Integer index = Integer.valueOf(readRequiredAttribute(revision, "index"));
        String fileName = readRequiredAttribute(revision, "file");
        return new AbstractMap.SimpleEntry<>(index, fileName);
    }

    private String readRequiredAttribute(Element commandElement, String attributeName) {
        String result = commandElement.getAttribute(attributeName);
        if (result.isEmpty()) {
            throw new IllegalStateException(attributeName + " attribute is required in " + commandElement.getTagName());
        }
        return result;
    }

}
