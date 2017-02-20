package com.helospark.sparktemplatingplugin.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

public class TemplatingToolDocumentProvider extends TextFileDocumentProvider {

    @Override
    public IDocument getDocument(Object element) {
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
    }
}