package org.camunda.bpm.run.utils;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.commons.logging.BaseLogger;

public class BpmRunLogger extends BaseLogger {
    public static final String PROJECT_CODE = "RUN";

    public static final String PROJECT_ID = "CR";

    public static final String PACKAGE = "org.camunda.bpm.run";

    public static final BpmRunLogger LOG = (BpmRunLogger) createLogger(BpmRunLogger.class, "RUN", "org.camunda.bpm.run", "CR");

    public void processEnginePluginRegistered(String pluginClass) {
        logInfo("001", "The process engine plugin '{}' was registered with the Camunda Run process engine.", new Object[]{pluginClass});
    }

    public ProcessEngineException failedProcessEnginePluginInstantiation(String pluginClass, Exception e) {
        return new ProcessEngineException(
                exceptionMessage("002", "Unable to register the process engine plugin '{}'. Please ensure that the correct plugin class is configured in your YAML configuration file, and that the class is present on the classpath. More details: {}", new Object[]{pluginClass, e.getMessage(), e}));
    }

    public ProcessEngineException pluginPropertyNotFound(String pluginName, String propertyName, Exception e) {
        return new ProcessEngineException(
                exceptionMessage("003", "Please check the configuration options for plugin '{}'. Some configuration parameters could not be found. More details: {}", new Object[]{pluginName, e.getMessage(), e}));
    }
}
