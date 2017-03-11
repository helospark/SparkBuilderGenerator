package com.helospark.sparktemplatingplugin.support.logging;

import java.util.Optional;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.helospark.sparktemplatingplugin.Activator;

public class PluginLogger {
    // Has to be lazy initialized, because DiContainer is initialized by the Activator
    // and logger can only be initialized after activator is initalized
    private Optional<ILog> logger = Optional.empty();
    private String ownerClassName;

    public PluginLogger(Class<?> owner) {
        this.ownerClassName = owner.getName();
    }

    public void info(String message) {
        logInternal(new Status(IStatus.INFO, Activator.PLUGIN_ID, composeExceptionMessage(message)));
    }

    public void warn(String message) {
        logInternal(new Status(IStatus.WARNING, Activator.PLUGIN_ID, composeExceptionMessage(message)));
    }

    public void warn(String message, Throwable exception) {
        logInternal(new Status(IStatus.WARNING, Activator.PLUGIN_ID, composeExceptionMessage(message), exception));
    }

    public void error(String message) {
        logInternal(new Status(IStatus.ERROR, Activator.PLUGIN_ID, composeExceptionMessage(message)));
    }

    public void error(String message, Throwable exception) {
        logInternal(new Status(IStatus.ERROR, Activator.PLUGIN_ID, composeExceptionMessage(message), exception));
    }

    private void logInternal(Status status) {
        lazyLoadLogger();
        if (logger.isPresent()) {
            logger.get().log(status);
        } else {
            // Should only happen in during testing, or before initialization completed
            System.out.println(status.getMessage());
        }
        logger.ifPresent(loggerInternal -> loggerInternal.log(status));
    }

    private void lazyLoadLogger() {
        if (!logger.isPresent()) {
            synchronized (logger) {
                if (!logger.isPresent()) {
                    logger = Optional.ofNullable(Activator.getDefault()).map(deafultActivator -> deafultActivator.getLog());
                }
            }
        }
    }

    private String composeExceptionMessage(String message) {
        return ownerClassName + " : " + message;
    }

}
