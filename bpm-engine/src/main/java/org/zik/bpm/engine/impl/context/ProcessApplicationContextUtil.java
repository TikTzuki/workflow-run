// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.context;

import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.util.ClassLoaderUtil;
import org.zik.bpm.engine.impl.application.ProcessApplicationManager;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.application.impl.ProcessApplicationLogger;

public class ProcessApplicationContextUtil
{
    private static final ProcessApplicationLogger LOG;
    
    public static ProcessApplicationReference getTargetProcessApplication(final CoreExecution execution) {
        if (execution instanceof ExecutionEntity) {
            return getTargetProcessApplication((ExecutionEntity)execution);
        }
        return getTargetProcessApplication((CaseExecutionEntity)execution);
    }
    
    public static ProcessApplicationReference getTargetProcessApplication(final ExecutionEntity execution) {
        if (execution == null) {
            return null;
        }
        final ProcessApplicationReference processApplicationForDeployment = getTargetProcessApplication(execution.getProcessDefinition());
        if (ProcessApplicationContextUtil.LOG.isContextSwitchLoggable() && processApplicationForDeployment == null) {
            loggContextSwitchDetails(execution);
        }
        return processApplicationForDeployment;
    }
    
    public static ProcessApplicationReference getTargetProcessApplication(final CaseExecutionEntity execution) {
        if (execution == null) {
            return null;
        }
        final ProcessApplicationReference processApplicationForDeployment = getTargetProcessApplication((ResourceDefinitionEntity)execution.getCaseDefinition());
        if (ProcessApplicationContextUtil.LOG.isContextSwitchLoggable() && processApplicationForDeployment == null) {
            loggContextSwitchDetails(execution);
        }
        return processApplicationForDeployment;
    }
    
    public static ProcessApplicationReference getTargetProcessApplication(final TaskEntity task) {
        if (task.getProcessDefinition() != null) {
            return getTargetProcessApplication(task.getProcessDefinition());
        }
        if (task.getCaseDefinition() != null) {
            return getTargetProcessApplication(task.getCaseDefinition());
        }
        return null;
    }
    
    public static ProcessApplicationReference getTargetProcessApplication(final ResourceDefinitionEntity definition) {
        ProcessApplicationReference reference = getTargetProcessApplication(definition.getDeploymentId());
        if (reference == null && areProcessApplicationsRegistered()) {
            for (ResourceDefinitionEntity previous = definition.getPreviousDefinition(); previous != null; previous = previous.getPreviousDefinition()) {
                reference = getTargetProcessApplication(previous.getDeploymentId());
                if (reference != null) {
                    return reference;
                }
            }
        }
        return reference;
    }
    
    public static ProcessApplicationReference getTargetProcessApplication(final String deploymentId) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final ProcessApplicationManager processApplicationManager = processEngineConfiguration.getProcessApplicationManager();
        final ProcessApplicationReference processApplicationForDeployment = processApplicationManager.getProcessApplicationForDeployment(deploymentId);
        return processApplicationForDeployment;
    }
    
    public static boolean areProcessApplicationsRegistered() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final ProcessApplicationManager processApplicationManager = processEngineConfiguration.getProcessApplicationManager();
        return processApplicationManager.hasRegistrations();
    }
    
    private static void loggContextSwitchDetails(final ExecutionEntity execution) {
        final CoreExecutionContext<? extends CoreExecution> executionContext = Context.getCoreExecutionContext();
        if (executionContext == null || executionContext.getExecution() != execution) {
            final ProcessApplicationManager processApplicationManager = Context.getProcessEngineConfiguration().getProcessApplicationManager();
            ProcessApplicationContextUtil.LOG.debugNoTargetProcessApplicationFound(execution, processApplicationManager);
        }
    }
    
    private static void loggContextSwitchDetails(final CaseExecutionEntity execution) {
        final CoreExecutionContext<? extends CoreExecution> executionContext = Context.getCoreExecutionContext();
        if (executionContext == null || executionContext.getExecution() != execution) {
            final ProcessApplicationManager processApplicationManager = Context.getProcessEngineConfiguration().getProcessApplicationManager();
            ProcessApplicationContextUtil.LOG.debugNoTargetProcessApplicationFoundForCaseExecution(execution, processApplicationManager);
        }
    }
    
    public static boolean requiresContextSwitch(final ProcessApplicationReference processApplicationReference) {
        final ProcessApplicationReference currentProcessApplication = Context.getCurrentProcessApplication();
        if (processApplicationReference == null) {
            return false;
        }
        if (currentProcessApplication == null) {
            return true;
        }
        if (!processApplicationReference.getName().equals(currentProcessApplication.getName())) {
            return true;
        }
        final ClassLoader processApplicationClassLoader = ProcessApplicationClassloaderInterceptor.getProcessApplicationClassLoader();
        final ClassLoader currentClassloader = ClassLoaderUtil.getContextClassloader();
        return currentClassloader != processApplicationClassLoader;
    }
    
    public static void doContextSwitch(final Runnable runnable, final ProcessDefinitionEntity contextDefinition) {
        final ProcessApplicationReference processApplication = getTargetProcessApplication(contextDefinition);
        if (requiresContextSwitch(processApplication)) {
            Context.executeWithinProcessApplication((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    runnable.run();
                    return null;
                }
            }, processApplication);
        }
        else {
            runnable.run();
        }
    }
    
    static {
        LOG = ProcessApplicationLogger.PROCESS_APPLICATION_LOGGER;
    }
}
