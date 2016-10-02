package com.helospark.spark.converter.handlers;

import java.util.List;
import java.util.Optional;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helospark.spark.converter.handlers.dialog.GenerateConverterDialog;
import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;

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
                    .withDestinationCompilationUnit(generateConverterDialog.getDestinationCompilationUnit())
                    .withSourceCompilationUnit(generateConverterDialog.getSourceCompilationUnit())
                    .withDestinationPackageFragment(generateConverterDialog.getDestinationPackageFragment())
                    .withRecursiveGeneration(generateConverterDialog.getRecursiveGeneration())
                    .withJavaProject(generateConverterDialog.getJavaProject())
                    .build());
        }
        return Optional.empty();
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
