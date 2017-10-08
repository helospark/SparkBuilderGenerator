package com.helospark.spark.builder;

import java.util.Optional;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;

/**
 * Logger support for the builder generator plugin.
 * @author helospark
 */
public class PluginLogger {
    private Optional<ILog> logger;

    public void info(String message) {
        Status messageToLog = new Status(Status.INFO, Activator.PLUGIN_ID, message);
        logMessage(messageToLog);
    }

    public void warn(String message) {
        Status messageToLog = new Status(Status.WARNING, Activator.PLUGIN_ID, message);
        logMessage(messageToLog);
    }

    public void warn(String message, Throwable exception) {
        Status messageToLog = new Status(Status.WARNING, Activator.PLUGIN_ID, message, exception);
        logMessage(messageToLog);
    }

    public void error(String message) {
        Status messageToLog = new Status(Status.ERROR, Activator.PLUGIN_ID, message);
        logMessage(messageToLog);
    }

    public void error(String message, Throwable exception) {
        Status messageToLog = new Status(Status.ERROR, Activator.PLUGIN_ID, message, exception);
        logMessage(messageToLog);
    }

    private void logMessage(Status messageToLog) {
        initializeLogger();
        if (logger.isPresent()) {
            logger.get().log(messageToLog);
        } else {
            // In some cases logger will be null, for example during integration test,
            // or before the logging system initialized, we can use regular console print then
            System.out.println(String.valueOf(messageToLog));
        }
    }

    private void initializeLogger() {
        if (logger == null) {
            synchronized (this) {
                if (logger == null) {
                    logger = Optional.ofNullable(Activator.getDefault())
                            .map(defaultActivator -> defaultActivator.getLog());
                }
            }
        }
    }

}
