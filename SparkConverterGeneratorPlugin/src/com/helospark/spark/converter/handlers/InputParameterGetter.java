package com.helospark.spark.converter.handlers;

import java.util.List;
import java.util.Optional;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helospark.spark.converter.handlers.dialog.GenerateConverterDialog;
import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.exception.PluginException;

/**
 * Gets input parameters from the user for the converter generation.
 * 
 * @author helospark
 */
public class InputParameterGetter {
    private DefaultCompilationUnitProvider defaultCompilationUnitProvider;

    public InputParameterGetter(DefaultCompilationUnitProvider defaultCompilationUnitProvider) {
        this.defaultCompilationUnitProvider = defaultCompilationUnitProvider;
    }

    public Optional<ConverterInputParameters> getInputParameters(Shell shell, ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        List<ICompilationUnit> compilationUnitTypes = defaultCompilationUnitProvider.getSelectedCompilationUnits(event, window);

        GenerateConverterDialog generateConverterDialog = new GenerateConverterDialog(window.getShell());
        if (compilationUnitTypes.size() >= 1) {
            generateConverterDialog.setSourceDefaultValue(compilationUnitTypes.get(0));
        }
        if (compilationUnitTypes.size() >= 2) {
            generateConverterDialog.setDestinationDefaultValue(compilationUnitTypes.get(1));
        }
        if (generateConverterDialog.open() == Dialog.OK) {
            if (!isDataValid(generateConverterDialog)) {
                displayMissingDataError(shell);
                return Optional.empty();
            }
            return Optional.of(ConverterInputParameters.builder()
                    .withDestinationType(extractRootType(generateConverterDialog.getDestinationCompilationUnit()))
                    .withSourceType(extractRootType(generateConverterDialog.getSourceCompilationUnit()))
                    .withDestinationPackageFragment(generateConverterDialog.getDestinationPackageFragment())
                    .withRecursiveGeneration(generateConverterDialog.getRecursiveGeneration())
                    .withJavaProject(generateConverterDialog.getJavaProject())
                    .build());
        }
        return Optional.empty();
    }

    private IType extractRootType(ICompilationUnit iCompilationUnit) {
        try {
            IType[] types = iCompilationUnit.getTypes();
            if (types.length == 0) {
                throw new PluginException("No class is present in " + iCompilationUnit.getElementName());
            }
            if (types.length > 1) {
                throw new PluginException("Multiple classes is present in " + iCompilationUnit.getElementName());
            }
            return types[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isDataValid(GenerateConverterDialog generateConverterDialog) {
        return !(generateConverterDialog.getDestinationCompilationUnit() == null ||
                generateConverterDialog.getSourceCompilationUnit() == null ||
                generateConverterDialog.getDestinationPackageFragment() == null ||
                generateConverterDialog.getRecursiveGeneration() == null);
    }

    private void displayMissingDataError(Shell shell) {
        MessageDialog.openError(shell, "Required data missing", "Please fill all fields");
    }

}
