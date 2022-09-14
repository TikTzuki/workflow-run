// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.application.ProcessApplicationManager;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.application.ProcessApplication;
import java.util.Iterator;
import javax.servlet.ServletException;
import java.util.Set;
import javax.naming.NamingException;
import org.zik.bpm.application.ProcessApplicationExecutionException;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class ProcessApplicationLogger extends ProcessEngineLogger
{
    public void taskNotRelatedToExecution(final DelegateTask delegateTask) {
        this.logDebug("001", "Task {} not related to an execution, target process application cannot be determined.", new Object[] { delegateTask });
    }
    
    public ProcessEngineException exceptionWhileNotifyingPaTaskListener(final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("002", "Exception while notifying process application task listener: " + e.getMessage(), new Object[0]), e);
    }
    
    public void noTargetProcessApplicationForExecution(final DelegateExecution execution) {
        this.logDebug("003", "No target process application found for execution {}", new Object[] { execution });
    }
    
    public void paDoesNotProvideExecutionListener(final String paName) {
        this.logDebug("004", "Target process application '{}' does not provide an ExecutionListener.", new Object[] { paName });
    }
    
    public void cannotInvokeListenerPaUnavailable(final String paName, final ProcessApplicationUnavailableException e) {
        this.logDebug("005", "Exception while invoking listener: target process application '{}' unavailable", new Object[] { paName, e });
    }
    
    public void paDoesNotProvideTaskListener(final String paName) {
        this.logDebug("006", "Target process application '{}' does not provide a TaskListener.", new Object[] { paName });
    }
    
    public void paElResolversDiscovered(final String summary) {
        this.logDebug("007", summary, new Object[0]);
    }
    
    public void noElResolverProvided(final String paName, final String string) {
        this.logWarn("008", "Process Application '{}': No ELResolver provided by ProcessApplicationElResolver {}", new Object[] { paName, string });
    }
    
    public ProcessApplicationExecutionException processApplicationExecutionException(final Exception e) {
        return new ProcessApplicationExecutionException(e);
    }
    
    public ProcessEngineException ejbPaCannotLookupSelfReference(final NamingException e) {
        return new ProcessEngineException(this.exceptionMessage("009", "Cannot lookup self reference to EjbProcessApplication", new Object[0]), e);
    }
    
    public ProcessEngineException ejbPaCannotAutodetectName(final NamingException e) {
        return new ProcessEngineException(this.exceptionMessage("010", "Could not autodetect EjbProcessApplicationName", new Object[0]), e);
    }
    
    public ProcessApplicationUnavailableException processApplicationUnavailableException(final String name, final Throwable cause) {
        return new ProcessApplicationUnavailableException(this.exceptionMessage("011", "Process Application '{}' unavailable", new Object[] { name }), cause);
    }
    
    public ProcessApplicationUnavailableException processApplicationUnavailableException(final String name) {
        return new ProcessApplicationUnavailableException(this.exceptionMessage("011", "Process Application '{}' unavailable", new Object[] { name }));
    }
    
    public void servletDeployerNoPaFound(final String ctxName) {
        this.logDebug("012", "Listener invoked for context '{}' but no process application annotation detected.", new Object[] { ctxName });
    }
    
    public ServletException multiplePasException(final Set<Class<?>> c, final String appId) {
        final StringBuilder builder = new StringBuilder();
        builder.append("An application must not contain more than one class annotated with @ProcessApplication.\n Application '");
        builder.append(appId);
        builder.append("' contains the following @ProcessApplication classes:\n");
        for (final Class<?> clazz : c) {
            builder.append("  ");
            builder.append(clazz.getName());
            builder.append("\n");
        }
        final String msg = builder.toString();
        return new ServletException(this.exceptionMessage("013", msg, new Object[0]));
    }
    
    public ServletException paWrongTypeException(final Class<?> paClass) {
        return new ServletException(this.exceptionMessage("014", "Class '{}' is annotated with @{} but is not a subclass of {}", new Object[] { paClass, ProcessApplication.class.getName(), AbstractProcessApplication.class.getName() }));
    }
    
    public void detectedPa(final Class<?> paClass) {
        this.logInfo("015", "Detected @ProcessApplication class '{}'", new Object[] { paClass.getName() });
    }
    
    public void alreadyDeployed() {
        this.logWarn("016", "Ignoring call of deploy() on process application that is already deployed.", new Object[0]);
    }
    
    public void notDeployed() {
        this.logWarn("017", "Calling undeploy() on process application that is not deployed.", new Object[0]);
    }
    
    public void couldNotRemoveDefinitionsFromCache(final Throwable t) {
        this.logError("018", "Unregistering process application for deployment but could not remove process definitions from deployment cache.", new Object[] { t });
    }
    
    public ProcessEngineException exceptionWhileRegisteringDeploymentsWithJobExecutor(final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("019", "Exception while registering deployment with job executor", new Object[0]), e);
    }
    
    public void exceptionWhileUnregisteringDeploymentsWithJobExecutor(final Exception e) {
        this.logError("020", "Exceptions while unregistering deployments with job executor", new Object[] { e });
    }
    
    public void registrationSummary(final String string) {
        this.logInfo("021", string, new Object[0]);
    }
    
    public void exceptionWhileLoggingRegistrationSummary(final Throwable e) {
        this.logError("022", "Exception while logging registration summary", new Object[] { e });
    }
    
    public boolean isContextSwitchLoggable() {
        return this.isDebugEnabled();
    }
    
    public void debugNoTargetProcessApplicationFound(final ExecutionEntity execution, final ProcessApplicationManager processApplicationManager) {
        this.logDebug("023", "no target process application found for Execution[{}], ProcessDefinition[{}], Deployment[{}] Registrations[{}]", new Object[] { execution.getId(), execution.getProcessDefinitionId(), execution.getProcessDefinition().getDeploymentId(), processApplicationManager.getRegistrationSummary() });
    }
    
    public void debugNoTargetProcessApplicationFoundForCaseExecution(final CaseExecutionEntity execution, final ProcessApplicationManager processApplicationManager) {
        this.logDebug("024", "no target process application found for CaseExecution[{}], CaseDefinition[{}], Deployment[{}] Registrations[{}]", new Object[] { execution.getId(), execution.getCaseDefinitionId(), ((CaseDefinitionEntity)execution.getCaseDefinition()).getDeploymentId(), processApplicationManager.getRegistrationSummary() });
    }
}
