// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionManager;
import java.util.Collection;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.HashMap;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionManager;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.SignalEventReceivedBuilderImpl;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SignalEventReceivedCmd implements Command<Void>
{
    protected static final CommandLogger LOG;
    protected final SignalEventReceivedBuilderImpl builder;
    
    public SignalEventReceivedCmd(final SignalEventReceivedBuilderImpl builder) {
        this.builder = builder;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final String signalName = this.builder.getSignalName();
        final String executionId = this.builder.getExecutionId();
        if (executionId == null) {
            this.sendSignal(commandContext, signalName);
        }
        else {
            this.sendSignalToExecution(commandContext, signalName, executionId);
        }
        return null;
    }
    
    protected void sendSignal(final CommandContext commandContext, final String signalName) {
        final List<EventSubscriptionEntity> signalEventSubscriptions = this.findSignalEventSubscriptions(commandContext, signalName);
        final List<EventSubscriptionEntity> catchSignalEventSubscription = this.filterIntermediateSubscriptions(signalEventSubscriptions);
        final List<EventSubscriptionEntity> startSignalEventSubscriptions = this.filterStartSubscriptions(signalEventSubscriptions);
        final Map<String, ProcessDefinitionEntity> processDefinitions = this.getProcessDefinitionsOfSubscriptions(startSignalEventSubscriptions);
        this.checkAuthorizationOfCatchSignals(commandContext, catchSignalEventSubscription);
        this.checkAuthorizationOfStartSignals(commandContext, startSignalEventSubscriptions, processDefinitions);
        this.notifyExecutions(catchSignalEventSubscription);
        this.startProcessInstances(startSignalEventSubscriptions, processDefinitions);
    }
    
    protected List<EventSubscriptionEntity> findSignalEventSubscriptions(final CommandContext commandContext, final String signalName) {
        final EventSubscriptionManager eventSubscriptionManager = commandContext.getEventSubscriptionManager();
        if (this.builder.isTenantIdSet()) {
            return eventSubscriptionManager.findSignalEventSubscriptionsByEventNameAndTenantId(signalName, this.builder.getTenantId());
        }
        return eventSubscriptionManager.findSignalEventSubscriptionsByEventName(signalName);
    }
    
    protected Map<String, ProcessDefinitionEntity> getProcessDefinitionsOfSubscriptions(final List<EventSubscriptionEntity> startSignalEventSubscriptions) {
        final DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
        final Map<String, ProcessDefinitionEntity> processDefinitions = new HashMap<String, ProcessDefinitionEntity>();
        for (final EventSubscriptionEntity eventSubscription : startSignalEventSubscriptions) {
            final String processDefinitionId = eventSubscription.getConfiguration();
            EnsureUtil.ensureNotNull("Configuration of signal start event subscription '" + eventSubscription.getId() + "' contains no process definition id.", (Object)processDefinitionId);
            final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);
            if (processDefinition != null && !processDefinition.isSuspended()) {
                processDefinitions.put(eventSubscription.getId(), processDefinition);
            }
        }
        return processDefinitions;
    }
    
    protected void sendSignalToExecution(final CommandContext commandContext, final String signalName, final String executionId) {
        final ExecutionManager executionManager = commandContext.getExecutionManager();
        final ExecutionEntity execution = executionManager.findExecutionById(executionId);
        EnsureUtil.ensureNotNull("Cannot find execution with id '" + executionId + "'", "execution", execution);
        final EventSubscriptionManager eventSubscriptionManager = commandContext.getEventSubscriptionManager();
        final List<EventSubscriptionEntity> signalEvents = eventSubscriptionManager.findSignalEventSubscriptionsByNameAndExecution(signalName, executionId);
        EnsureUtil.ensureNotEmpty("Execution '" + executionId + "' has not subscribed to a signal event with name '" + signalName + "'.", signalEvents);
        this.checkAuthorizationOfCatchSignals(commandContext, signalEvents);
        this.notifyExecutions(signalEvents);
    }
    
    protected void checkAuthorizationOfCatchSignals(final CommandContext commandContext, final List<EventSubscriptionEntity> catchSignalEventSubscription) {
        for (final EventSubscriptionEntity event : catchSignalEventSubscription) {
            final String processInstanceId = event.getProcessInstanceId();
            for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                checker.checkUpdateProcessInstanceById(processInstanceId);
            }
        }
    }
    
    private void checkAuthorizationOfStartSignals(final CommandContext commandContext, final List<EventSubscriptionEntity> startSignalEventSubscriptions, final Map<String, ProcessDefinitionEntity> processDefinitions) {
        for (final EventSubscriptionEntity signalStartEventSubscription : startSignalEventSubscriptions) {
            final ProcessDefinitionEntity processDefinition = processDefinitions.get(signalStartEventSubscription.getId());
            if (processDefinition != null) {
                for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                    checker.checkCreateProcessInstance(processDefinition);
                }
            }
        }
    }
    
    private void notifyExecutions(final List<EventSubscriptionEntity> catchSignalEventSubscription) {
        for (final EventSubscriptionEntity signalEventSubscriptionEntity : catchSignalEventSubscription) {
            if (this.isActiveEventSubscription(signalEventSubscriptionEntity)) {
                signalEventSubscriptionEntity.eventReceived(this.builder.getVariables(), false);
            }
        }
    }
    
    private boolean isActiveEventSubscription(final EventSubscriptionEntity signalEventSubscriptionEntity) {
        final ExecutionEntity execution = signalEventSubscriptionEntity.getExecution();
        return !execution.isEnded() && !execution.isCanceled();
    }
    
    private void startProcessInstances(final List<EventSubscriptionEntity> startSignalEventSubscriptions, final Map<String, ProcessDefinitionEntity> processDefinitions) {
        for (final EventSubscriptionEntity signalStartEventSubscription : startSignalEventSubscriptions) {
            final ProcessDefinitionEntity processDefinition = processDefinitions.get(signalStartEventSubscription.getId());
            if (processDefinition != null) {
                final ActivityImpl signalStartEvent = processDefinition.findActivity(signalStartEventSubscription.getActivityId());
                final PvmProcessInstance processInstance = processDefinition.createProcessInstanceForInitial(signalStartEvent);
                processInstance.start((Map<String, Object>)this.builder.getVariables());
            }
        }
    }
    
    protected List<EventSubscriptionEntity> filterIntermediateSubscriptions(final List<EventSubscriptionEntity> subscriptions) {
        final List<EventSubscriptionEntity> result = new ArrayList<EventSubscriptionEntity>();
        for (final EventSubscriptionEntity subscription : subscriptions) {
            if (subscription.getExecutionId() != null) {
                result.add(subscription);
            }
        }
        return result;
    }
    
    protected List<EventSubscriptionEntity> filterStartSubscriptions(final List<EventSubscriptionEntity> subscriptions) {
        final List<EventSubscriptionEntity> result = new ArrayList<EventSubscriptionEntity>();
        for (final EventSubscriptionEntity subscription : subscriptions) {
            if (subscription.getExecutionId() == null) {
                result.add(subscription);
            }
        }
        return result;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
