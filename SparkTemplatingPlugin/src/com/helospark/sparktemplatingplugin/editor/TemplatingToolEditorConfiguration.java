package com.helospark.sparktemplatingplugin.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.JavaColorManager;
import org.eclipse.jdt.internal.ui.text.PreferencesAdapter;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

public class TemplatingToolEditorConfiguration extends SourceViewerConfiguration {
    private TemplatingToolTemplateScanner scanner;
    private IColorManager colorManager;

    public TemplatingToolEditorConfiguration(IColorManager colorManager) {
        this.colorManager = colorManager;
    }

    @Override
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return new String[] {
                IDocument.DEFAULT_CONTENT_TYPE,
                TemplatingToolPartitionScanner.PROGRAM_SOURCE };
    }

    protected TemplatingToolTemplateScanner getXMLScanner() {
        if (scanner == null) {
            scanner = new TemplatingToolTemplateScanner(colorManager);
            scanner.setDefaultReturnToken(
                    new Token(
                            new TextAttribute(
                                    colorManager.getColor(TemplatingToolColorConstants.DEFAULT))));
        }
        return scanner;
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();

        IPreferenceStore preferenceStore = createCombinedPreferenceStore();
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new TemplatingToolBeanShellCodeScanner(new JavaColorManager(), preferenceStore));
        reconciler.setDamager(dr, TemplatingToolPartitionScanner.PROGRAM_SOURCE);
        reconciler.setRepairer(dr, TemplatingToolPartitionScanner.PROGRAM_SOURCE);

        dr = new DefaultDamagerRepairer(getXMLScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        return reconciler;
    }

    private IPreferenceStore createCombinedPreferenceStore() {
        List stores = new ArrayList(3);

        stores.add(JavaPlugin.getDefault().getPreferenceStore());
        stores.add(new PreferencesAdapter(JavaPlugin.getJavaCorePluginPreferences()));
        stores.add(EditorsUI.getPreferenceStore());
        stores.add(PlatformUI.getPreferenceStore());

        return new ChainedPreferenceStore((IPreferenceStore[]) stores.toArray(new IPreferenceStore[stores.size()]));
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant ca = new ContentAssistant();
        IContentAssistProcessor cap = new TemplatingToolCompletionProcessor();
        ca.setContentAssistProcessor(cap,
                IDocument.DEFAULT_CONTENT_TYPE);
        ca.setInformationControlCreator(
                getInformationControlCreator(sourceViewer));
        return ca;
    }

}