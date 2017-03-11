package com.helospark.sparktemplatingplugin.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;

public class TemplatingToolDocumentProvider extends TextFileDocumentProvider {
    private static final PluginLogger LOGGER = new PluginLogger(TemplatingToolDocumentProvider.class);

    @Override
    public IDocument getDocument(Object element) {
        try {
            IDocument document = super.getDocument(element);
            if (document != null) {
                IDocumentPartitioner partitioner = new FastPartitioner(
                        new TemplatingToolPartitionScanner(),
                        new String[] {
                                TemplatingToolPartitionScanner.PROGRAM_SOURCE });
                partitioner.connect(document);
                document.setDocumentPartitioner(partitioner);
            }
            return document;
        } catch (Exception e) {
            LOGGER.error("Unable to provide document for the editor", e);
            throw new RuntimeException(e);
        }
    }
}