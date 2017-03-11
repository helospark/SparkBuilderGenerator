package com.helospark.sparktemplatingplugin.execute.templater.provider;

import java.util.Optional;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helospark.sparktemplatingplugin.execute.templater.ScriptExposedProvider;
import com.helospark.sparktemplatingplugin.support.logging.PluginLogger;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttType;
import com.helospark.sparktemplatingplugin.wrapper.impl.SttTypeImpl;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.NullSttType;

public class CurrentClassProvider implements ScriptExposedProvider {
    public static final String EXPOSED_NAME = "currentClass";
    private static final PluginLogger LOGGER = new PluginLogger(CurrentClassProvider.class);
    private CompilationUnitProvider compilationUnitProvider;

    public CurrentClassProvider(CompilationUnitProvider compilationUnitProvider) {
        this.compilationUnitProvider = compilationUnitProvider;
    }

    @Override
    public Object provide(ExecutionEvent event) {
        try {
            Optional<IType> result = Optional.empty();
            SttCompilationUnit compilationUnit = compilationUnitProvider.provideCurrentICompiltionUnit(event);
            if (compilationUnit.isPresent()) {
                ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
                if (currentSelection instanceof ITextSelection) {
                    ITextSelection selection = (ITextSelection) currentSelection;
                    IJavaElement element = compilationUnit.getRaw().getElementAt(selection.getOffset());
                    if (element != null) {
                        result = Optional.ofNullable((IType) element.getAncestor(IJavaElement.TYPE));
                    } else {
                        result = Optional.ofNullable(compilationUnit.getRaw().findPrimaryType());
                    }
                } else if (compilationUnit.getRaw().getAllTypes().length == 1) {
                    result = Optional.ofNullable(compilationUnit.getRaw().findPrimaryType());
                }
            }

            return result
                    .map(r -> createType(r))
                    .orElse(new NullSttType("No type is currently selected"));
        } catch (Exception e) {
            LOGGER.error("Unexpected error while extracting currently selected class", e);
            return new NullSttType("Unexpected error while extracting currently selected class");
        }
    }

    private SttType createType(IType r) {
        return new SttTypeImpl(r);
    }

    @Override
    public Class<?> getExposedObjectType() {
        return SttType.class;
    }

    @Override
    public String getExposedName() {
        return EXPOSED_NAME;
    }
}
