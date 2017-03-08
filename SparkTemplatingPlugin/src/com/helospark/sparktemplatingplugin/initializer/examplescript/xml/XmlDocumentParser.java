package com.helospark.sparktemplatingplugin.initializer.examplescript.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class XmlDocumentParser {

    public Document parse(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            return document;
        } catch (Exception e) {
            throw new RuntimeException("Cannot load " + file.getAbsolutePath(), e);
        }
    }
}
