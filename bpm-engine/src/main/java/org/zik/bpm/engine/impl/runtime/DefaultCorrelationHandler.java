// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionManager;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.Collections;
import java.util.Iterator;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.runtime.Execution;
import org.zik.bpm.engine.impl.Page;
import java.util.Map;
import org.zik.bpm.engine.impl.ExecutionQueryImpl;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class DefaultCorrelationHandler implements CorrelationHandler
{
    private static final CommandLogger LOG;
    
    @Override
    public CorrelationHandlerResult correlateMessage(final CommandContext commandContext, final String messageName, final CorrelationSet correlationSet) {
        List<CorrelationHandlerResult> correlations = this.correlateMessageToExecutions(commandContext, messageName, correlationSet);
        if (correlations.size() > 1) {
            throw DefaultCorrelationHandler.LOG.exceptionCorrelateMessageToSingleExecution(messageName, correlations.size(), correlationSet);
        }
        if (correlations.size() == 1) {
            return correlations.get(0);
        }
        if (correlationSet.isExecutionsOnly()) {
            return null;
        }
        correlations = this.correlateStartMessages(commandContext, messageName, correlationSet);
        if (correlations.size() > 1) {
            throw DefaultCorrelationHandler.LOG.exceptionCorrelateMessageToSingleProcessDefinition(messageName, correlations.size(), correlationSet);
        }
        if (correlations.size() == 1) {
            return correlations.get(0);
        }
        return null;
    }
    
    @Override
    public List<CorrelationHandlerResult> correlateMessages(final CommandContext commandContext, final String messageName, final CorrelationSet correlationSet) {
        final List<CorrelationHandlerResult> results = new ArrayList<CorrelationHandlerResult>();
        results.addAll(this.correlateMessageToExecutions(commandContext, messageName, correlationSet));
        if (!correlationSet.isExecutionsOnly()) {
            results.addAll(this.correlateStartMessages(commandContext, messageName, correlationSet));
        }
        return results;
    }
    
    protected List<CorrelationHandlerResult> correlateMessageToExecutions(final CommandContext commandContext, final String messageName, final CorrelationSet correlationSet) {
        final ExecutionQueryImpl query = new ExecutionQueryImpl();
        final Map<String, Object> correlationKeys = correlationSet.getCorrelationKeys();
        if (correlationKeys != null) {
            for (final Map.Entry<String, Object> correlationKey : correlationKeys.entrySet()) {
                query.processVariableValueEquals(correlationKey.getKey(), correlationKey.getValue());
            }
        }
        final Map<String, Object> localCorrelationKeys = correlationSet.getLocalCorrelationKeys();
        if (localCorrelationKeys != null) {
            for (final Map.Entry<String, Object> correlationKey2 : localCorrelationKeys.entrySet()) {
                query.variableValueEquals(correlationKey2.getKey(), correlationKey2.getValue());
            }
        }
        final String businessKey = correlationSet.getBusinessKey();
        if (businessKey != null) {
            query.processInstanceBusinessKey(businessKey);
        }
        final String processInstanceId = correlationSet.getProcessInstanceId();
        if (processInstanceId != null) {
            query.processInstanceId(processInstanceId);
        }
        if (messageName != null) {
            query.messageEventSubscriptionName(messageName);
        }
        else {
            query.messageEventSubscription();
        }
        if (correlationSet.isTenantIdSet) {
            final String tenantId = correlationSet.getTenantId();
            if (tenantId != null) {
                query.tenantIdIn(tenantId);
            }
            else {
                query.withoutTenantId();
            }
        }
        query.active();
        final List<Execution> matchingExecutions = ((AbstractQuery<T, Execution>)query).evaluateExpressionsAndExecuteList(commandContext, null);
        final List<CorrelationHandlerResult> result = new ArrayList<CorrelationHandlerResult>(matchingExecutions.size());
        for (final Execution matchingExecution : matchingExecutions) {
            final CorrelationHandlerResult correlationResult = CorrelationHandlerResult.matchedExecution((ExecutionEntity)matchingExecution);
            if (!commandContext.getDbEntityManager().isDeleted(correlationResult.getExecutionEntity())) {
                result.add(correlationResult);
            }
        }
        return result;
    }
    
    @Override
    public List<CorrelationHandlerResult> correlateStartMessages(final CommandContext commandContext, final String messageName, final CorrelationSet correlationSet) {
        if (messageName == null) {
            return Collections.emptyList();
        }
        if (correlationSet.getProcessDefinitionId() == null) {
            return this.correlateStartMessageByEventSubscription(commandContext, messageName, correlationSet);
        }
        final CorrelationHandlerResult correlationResult = this.correlateStartMessageByProcessDefinitionId(commandContext, messageName, correlationSet.getProcessDefinitionId());
        if (correlationResult != null) {
            return Collections.singletonList(correlationResult);
        }
        return Collections.emptyList();
    }
    
    protected List<CorrelationHandlerResult> correlateStartMessageByEventSubscription(final CommandContext commandContext, final String messageName, final CorrelationSet correlationSet) {
        final List<CorrelationHandlerResult> results = new ArrayList<CorrelationHandlerResult>();
        final DeploymentCache deploymentCache = commandContext.getProcessEngineConfiguration().getDeploymentCache();
        final List<EventSubscriptionEntity> messageEventSubscriptions = this.findMessageStartEventSubscriptions(commandContext, messageName, correlationSet);
        for (final EventSubscriptionEntity messageEventSubscription : messageEventSubscriptions) {
            if (messageEventSubscription.getConfiguration() != null) {
                final String processDefinitionId = messageEventSubscription.getConfiguration();
                final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);
                if (processDefinition != null && !processDefinition.isSuspended()) {
                    final CorrelationHandlerResult result = CorrelationHandlerResult.matchedProcessDefinition(processDefinition, messageEventSubscription.getActivityId());
                    results.add(result);
                }
                else {
                    DefaultCorrelationHandler.LOG.couldNotFindProcessDefinitionForEventSubscription(messageEventSubscription, processDefinitionId);
                }
            }
        }
        return results;
    }
    
    protected List<EventSubscriptionEntity> findMessageStartEventSubscriptions(final CommandContext commandContext, final String messageName, final CorrelationSet correlationSet) {
        final EventSubscriptionManager eventSubscriptionManager = commandContext.getEventSubscriptionManager();
        if (!correlationSet.isTenantIdSet) {
            return eventSubscriptionManager.findMessageStartEventSubscriptionByName(messageName);
        }
        final EventSubscriptionEntity eventSubscription = eventSubscriptionManager.findMessageStartEventSubscriptionByNameAndTenantId(messageName, correlationSet.getTenantId());
        if (eventSubscription != null) {
            return Collections.singletonList(eventSubscription);
        }
        return Collections.emptyList();
    }
    
    protected CorrelationHandlerResult correlateStartMessageByProcessDefinitionId(final CommandContext commandContext, final String messageName, final String processDefinitionId) {
        final DeploymentCache deploymentCache = commandContext.getProcessEngineConfiguration().getDeploymentCache();
        final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);
        if (processDefinition != null && !processDefinition.isSuspended()) {
            final String startActivityId = this.findStartActivityIdByMessage(processDefinition, messageName);
            if (startActivityId != null) {
                return CorrelationHandlerResult.matchedProcessDefinition(processDefinition, startActivityId);
            }
        }
        return null;
    }
    
    protected String findStartActivityIdByMessage(final ProcessDefinitionEntity processDefinition, final String messageName) {
        for (final EventSubscriptionDeclaration declaration : EventSubscriptionDeclaration.getDeclarationsForScope(processDefinition).values()) {
            if (this.isMessageStartEventWithName(declaration, messageName)) {
                return declaration.getActivityId();
            }
        }
        return null;
    }
    
    protected boolean isMessageStartEventWithName(final EventSubscriptionDeclaration declaration, final String messageName) {
        return EventType.MESSAGE.name().equals(declaration.getEventType()) && declaration.isStartEvent() && messageName.equals(declaration.getUnresolvedEventName());
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
