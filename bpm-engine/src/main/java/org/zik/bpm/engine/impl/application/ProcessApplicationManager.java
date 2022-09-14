// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.application;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionManager;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.ArrayList;
import java.util.Collection;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import org.zik.bpm.engine.impl.persistence.deploy.DeploymentFailListener;
import java.util.Iterator;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.application.ProcessApplicationRegistration;
import java.util.Set;
import org.zik.bpm.application.ProcessApplicationReference;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.application.impl.ProcessApplicationLogger;

public class ProcessApplicationManager
{
    public static final ProcessApplicationLogger LOG;
    protected Map<String, DefaultProcessApplicationRegistration> registrationsByDeploymentId;
    
    public ProcessApplicationManager() {
        this.registrationsByDeploymentId = new HashMap<String, DefaultProcessApplicationRegistration>();
    }
    
    public ProcessApplicationReference getProcessApplicationForDeployment(final String deploymentId) {
        final DefaultProcessApplicationRegistration registration = this.registrationsByDeploymentId.get(deploymentId);
        if (registration != null) {
            return registration.getReference();
        }
        return null;
    }
    
    public synchronized ProcessApplicationRegistration registerProcessApplicationForDeployments(final Set<String> deploymentsToRegister, final ProcessApplicationReference reference) {
        final DefaultProcessApplicationRegistration registration = this.createProcessApplicationRegistration(deploymentsToRegister, reference);
        this.createJobExecutorRegistrations(deploymentsToRegister);
        this.logRegistration(deploymentsToRegister, reference);
        return registration;
    }
    
    public synchronized void clearRegistrations() {
        this.registrationsByDeploymentId.clear();
    }
    
    public synchronized void unregisterProcessApplicationForDeployments(final Set<String> deploymentIds, final boolean removeProcessesFromCache) {
        this.removeJobExecutorRegistrations(deploymentIds);
        this.removeProcessApplicationRegistration(deploymentIds, removeProcessesFromCache);
    }
    
    public boolean hasRegistrations() {
        return !this.registrationsByDeploymentId.isEmpty();
    }
    
    protected DefaultProcessApplicationRegistration createProcessApplicationRegistration(final Set<String> deploymentsToRegister, final ProcessApplicationReference reference) {
        final String processEngineName = Context.getProcessEngineConfiguration().getProcessEngineName();
        final DefaultProcessApplicationRegistration registration = new DefaultProcessApplicationRegistration(reference, deploymentsToRegister, processEngineName);
        for (final String deploymentId : deploymentsToRegister) {
            this.registrationsByDeploymentId.put(deploymentId, registration);
        }
        return registration;
    }
    
    protected void removeProcessApplicationRegistration(final Set<String> deploymentIds, final boolean removeProcessesFromCache) {
        for (final String deploymentId : deploymentIds) {
            try {
                if (!removeProcessesFromCache) {
                    continue;
                }
                Context.getProcessEngineConfiguration().getDeploymentCache().removeDeployment(deploymentId);
            }
            catch (Throwable t) {
                ProcessApplicationManager.LOG.couldNotRemoveDefinitionsFromCache(t);
            }
            finally {
                if (deploymentId != null) {
                    this.registrationsByDeploymentId.remove(deploymentId);
                }
            }
        }
    }
    
    protected void createJobExecutorRegistrations(final Set<String> deploymentIds) {
        try {
            final DeploymentFailListener deploymentFailListener = new DeploymentFailListener(deploymentIds, Context.getProcessEngineConfiguration().getCommandExecutorTxRequiresNew());
            Context.getCommandContext().getTransactionContext().addTransactionListener(TransactionState.ROLLED_BACK, deploymentFailListener);
            final Set<String> registeredDeployments = Context.getProcessEngineConfiguration().getRegisteredDeployments();
            registeredDeployments.addAll(deploymentIds);
        }
        catch (Exception e) {
            throw ProcessApplicationManager.LOG.exceptionWhileRegisteringDeploymentsWithJobExecutor(e);
        }
    }
    
    protected void removeJobExecutorRegistrations(final Set<String> deploymentIds) {
        try {
            final Set<String> registeredDeployments = Context.getProcessEngineConfiguration().getRegisteredDeployments();
            registeredDeployments.removeAll(deploymentIds);
        }
        catch (Exception e) {
            ProcessApplicationManager.LOG.exceptionWhileUnregisteringDeploymentsWithJobExecutor(e);
        }
    }
    
    protected void logRegistration(final Set<String> deploymentIds, final ProcessApplicationReference reference) {
        if (!ProcessApplicationManager.LOG.isInfoEnabled()) {
            return;
        }
        try {
            final StringBuilder builder = new StringBuilder();
            builder.append("ProcessApplication '");
            builder.append(reference.getName());
            builder.append("' registered for DB deployments ");
            builder.append(deploymentIds);
            builder.append(". ");
            final List<ProcessDefinition> processDefinitions = new ArrayList<ProcessDefinition>();
            final List<CaseDefinition> caseDefinitions = new ArrayList<CaseDefinition>();
            final CommandContext commandContext = Context.getCommandContext();
            final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
            final boolean cmmnEnabled = processEngineConfiguration.isCmmnEnabled();
            for (final String deploymentId : deploymentIds) {
                final DeploymentEntity deployment = commandContext.getDbEntityManager().selectById(DeploymentEntity.class, deploymentId);
                if (deployment != null) {
                    processDefinitions.addAll(this.getDeployedProcessDefinitionArtifacts(deployment));
                    if (!cmmnEnabled) {
                        continue;
                    }
                    caseDefinitions.addAll(this.getDeployedCaseDefinitionArtifacts(deployment));
                }
            }
            this.logProcessDefinitionRegistrations(builder, processDefinitions);
            if (cmmnEnabled) {
                this.logCaseDefinitionRegistrations(builder, caseDefinitions);
            }
            ProcessApplicationManager.LOG.registrationSummary(builder.toString());
        }
        catch (Throwable e) {
            ProcessApplicationManager.LOG.exceptionWhileLoggingRegistrationSummary(e);
        }
    }
    
    protected List<ProcessDefinition> getDeployedProcessDefinitionArtifacts(final DeploymentEntity deployment) {
        final CommandContext commandContext = Context.getCommandContext();
        final List<ProcessDefinition> entities = deployment.getDeployedProcessDefinitions();
        if (entities == null) {
            final String deploymentId = deployment.getId();
            final ProcessDefinitionManager manager = commandContext.getProcessDefinitionManager();
            return manager.findProcessDefinitionsByDeploymentId(deploymentId);
        }
        return entities;
    }
    
    protected List<CaseDefinition> getDeployedCaseDefinitionArtifacts(final DeploymentEntity deployment) {
        final CommandContext commandContext = Context.getCommandContext();
        final List<CaseDefinition> entities = deployment.getDeployedCaseDefinitions();
        if (entities == null) {
            final String deploymentId = deployment.getId();
            final CaseDefinitionManager caseDefinitionManager = commandContext.getCaseDefinitionManager();
            return caseDefinitionManager.findCaseDefinitionByDeploymentId(deploymentId);
        }
        return entities;
    }
    
    protected void logProcessDefinitionRegistrations(final StringBuilder builder, final List<ProcessDefinition> processDefinitions) {
        if (processDefinitions.isEmpty()) {
            builder.append("Deployment does not provide any process definitions.");
        }
        else {
            builder.append("Will execute process definitions ");
            builder.append("\n");
            for (final ProcessDefinition processDefinition : processDefinitions) {
                builder.append("\n");
                builder.append("        ");
                builder.append(processDefinition.getKey());
                builder.append("[version: ");
                builder.append(processDefinition.getVersion());
                builder.append(", id: ");
                builder.append(processDefinition.getId());
                builder.append("]");
            }
            builder.append("\n");
        }
    }
    
    protected void logCaseDefinitionRegistrations(final StringBuilder builder, final List<CaseDefinition> caseDefinitions) {
        if (caseDefinitions.isEmpty()) {
            builder.append("Deployment does not provide any case definitions.");
        }
        else {
            builder.append("\n");
            builder.append("Will execute case definitions ");
            builder.append("\n");
            for (final CaseDefinition caseDefinition : caseDefinitions) {
                builder.append("\n");
                builder.append("        ");
                builder.append(caseDefinition.getKey());
                builder.append("[version: ");
                builder.append(caseDefinition.getVersion());
                builder.append(", id: ");
                builder.append(caseDefinition.getId());
                builder.append("]");
            }
            builder.append("\n");
        }
    }
    
    public String getRegistrationSummary() {
        final StringBuilder builder = new StringBuilder();
        for (final Map.Entry<String, DefaultProcessApplicationRegistration> entry : this.registrationsByDeploymentId.entrySet()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(entry.getKey());
            builder.append("->");
            builder.append(entry.getValue().getReference().getName());
        }
        return builder.toString();
    }
    
    static {
        LOG = ProcessEngineLogger.PROCESS_APPLICATION_LOGGER;
    }
}
