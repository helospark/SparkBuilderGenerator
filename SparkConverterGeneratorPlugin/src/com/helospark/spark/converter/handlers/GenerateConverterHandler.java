package com.helospark.spark.converter.handlers;

import static com.helospark.spark.converter.Activator.getDependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.helospark.spark.converter.Activator;
import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.service.ConverterGenerator;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class GenerateConverterHandler extends AbstractHandler {
    private InputParameterGetter inputParameterGetter;
    private ConverterGenerator converterGenerator;

    /**
     * The constructor.
     */
    public GenerateConverterHandler() {
        this(getDependency(InputParameterGetter.class), getDependency(ConverterGenerator.class));
    }

    public GenerateConverterHandler(InputParameterGetter inputParameterGetter, ConverterGenerator converterGenerator) {
        this.inputParameterGetter = inputParameterGetter;
        this.converterGenerator = converterGenerator;
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            Optional<ConverterInputParameters> inputParameters = inputParameterGetter.getInputParameters(getShell(), event);
            if (inputParameters.isPresent()) {
                converterGenerator.generate(inputParameters.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            createErrorDialog(e);
        }
        return null;
    }

    private void createErrorDialog(Exception e) {
        MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);
        ErrorDialog.openError(getShell(), "Error",
                "This error should not have happened!\n" +
                        "You can create an issue on https://github.com/helospark/SparkTools with the below stacktrace",
                status);
    }

    private static MultiStatus createMultiStatus(String msg, Exception exception) {
        List<Status> childStatuses = new ArrayList<>();
        for (StackTraceElement stackTrace : exception.getStackTrace()) {
            childStatuses.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, stackTrace.toString()));
        }

        return new MultiStatus(Activator.PLUGIN_ID,
                IStatus.ERROR, childStatuses.toArray(new Status[] {}),
                exception.toString(), exception);
    }

    private Shell getShell() {
        return PlatformUI.getWorkbench().getDisplay().getActiveShell();
    }
}
