// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.deployer;

import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.AbstractDefinitionDeployer;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParseLogger;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParser;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.zik.bpm.engine.impl.cmd.DeleteJobsCmd;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.model.Properties;
import org.zik.bpm.engine.impl.core.model.PropertyMapKey;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.persistence.entity.*;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.management.JobDefinition;
import org.zik.bpm.engine.repository.ProcessDefinition;

import java.io.ByteArrayInputStream;
import java.util.*;

public class BpmnDeployer extends AbstractDefinitionDeployer<ProcessDefinitionEntity> {
    public static BpmnParseLogger LOG;
    public static final String[] BPMN_RESOURCE_SUFFIXES;
    protected static final PropertyMapKey<String, List<JobDeclaration<?, ?>>> JOB_DECLARATIONS_PROPERTY;
    protected ExpressionManager expressionManager;
    protected BpmnParser bpmnParser;

    @Override
    protected String[] getResourcesSuffixes() {
        return BpmnDeployer.BPMN_RESOURCE_SUFFIXES;
    }

    @Override
    protected List<ProcessDefinitionEntity> transformDefinitions(final DeploymentEntity deployment, final ResourceEntity resource, final Properties properties) {
        final byte[] bytes = resource.getBytes();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        final BpmnParse bpmnParse = this.bpmnParser.createParse().sourceInputStream(inputStream).deployment(deployment).name(resource.getName());
        if (!deployment.isValidatingSchema()) {
            bpmnParse.setSchemaResource(null);
        }
        bpmnParse.execute();
        if (!properties.contains(BpmnDeployer.JOB_DECLARATIONS_PROPERTY)) {
            properties.set(
//                    (PropertyMapKey<Object, Object>)
                    BpmnDeployer.JOB_DECLARATIONS_PROPERTY,
                    new HashMap<>()
            );
        }
        properties.get(BpmnDeployer.JOB_DECLARATIONS_PROPERTY).putAll(bpmnParse.getJobDeclarations());
        return bpmnParse.getProcessDefinitions();
    }

    @Override
    protected ProcessDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        return this.getProcessDefinitionManager().findProcessDefinitionByDeploymentAndKey(deploymentId, definitionKey);
    }

    @Override
    protected ProcessDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        return this.getProcessDefinitionManager().findLatestProcessDefinitionByKeyAndTenantId(definitionKey, tenantId);
    }

    @Override
    protected void persistDefinition(final ProcessDefinitionEntity definition) {
        this.getProcessDefinitionManager().insertProcessDefinition(definition);
    }

    @Override
    protected void addDefinitionToDeploymentCache(final DeploymentCache deploymentCache, final ProcessDefinitionEntity definition) {
        deploymentCache.addProcessDefinition(definition);
    }

    @Override
    protected void definitionAddedToDeploymentCache(final DeploymentEntity deployment, final ProcessDefinitionEntity definition, final Properties properties) {
        final List<JobDeclaration<?, ?>> declarations = properties.get(BpmnDeployer.JOB_DECLARATIONS_PROPERTY).get(definition.getKey());
        this.updateJobDeclarations(declarations, definition, deployment.isNew());
        final ProcessDefinitionEntity latestDefinition = this.findLatestDefinitionByKeyAndTenantId(definition.getKey(), definition.getTenantId());
        if (deployment.isNew()) {
            this.adjustStartEventSubscriptions(definition, latestDefinition);
        }
        this.addAuthorizations(definition);
    }

    @Override
    protected void persistedDefinitionLoaded(final DeploymentEntity deployment, final ProcessDefinitionEntity definition, final ProcessDefinitionEntity persistedDefinition) {
        definition.setSuspensionState(persistedDefinition.getSuspensionState());
    }

    @Override
    protected void handlePersistedDefinition(final ProcessDefinitionEntity definition, final ProcessDefinitionEntity persistedDefinition, final DeploymentEntity deployment, final Properties properties) {
        if (persistedDefinition != null) {
            super.handlePersistedDefinition(definition, persistedDefinition, deployment, properties);
        }
    }

    protected void updateJobDeclarations(final List<JobDeclaration<?, ?>> jobDeclarations, final ProcessDefinitionEntity processDefinition, final boolean isNewDeployment) {
        if (jobDeclarations == null || jobDeclarations.isEmpty()) {
            return;
        }
        final JobDefinitionManager jobDefinitionManager = this.getJobDefinitionManager();
        if (isNewDeployment) {
            for (final JobDeclaration<?, ?> jobDeclaration : jobDeclarations) {
                this.createJobDefinition(processDefinition, jobDeclaration);
            }
        } else {
            final List<JobDefinitionEntity> existingDefinitions = jobDefinitionManager.findByProcessDefinitionId(processDefinition.getId());
            LegacyBehavior.migrateMultiInstanceJobDefinitions(processDefinition, existingDefinitions);
            for (final JobDeclaration<?, ?> jobDeclaration2 : jobDeclarations) {
                boolean jobDefinitionExists = false;
                for (final JobDefinition jobDefinitionEntity : existingDefinitions) {
                    if (jobDeclaration2.getActivityId().equals(jobDefinitionEntity.getActivityId()) && jobDeclaration2.getJobHandlerType().equals(jobDefinitionEntity.getJobType())) {
                        jobDeclaration2.setJobDefinitionId(jobDefinitionEntity.getId());
                        jobDefinitionExists = true;
                        break;
                    }
                }
                if (!jobDefinitionExists) {
                    this.createJobDefinition(processDefinition, jobDeclaration2);
                }
            }
        }
    }

    protected void createJobDefinition(final ProcessDefinition processDefinition, final JobDeclaration<?, ?> jobDeclaration) {
        final JobDefinitionManager jobDefinitionManager = this.getJobDefinitionManager();
        final JobDefinitionEntity jobDefinitionEntity = new JobDefinitionEntity(jobDeclaration);
        jobDefinitionEntity.setProcessDefinitionId(processDefinition.getId());
        jobDefinitionEntity.setProcessDefinitionKey(processDefinition.getKey());
        jobDefinitionEntity.setTenantId(processDefinition.getTenantId());
        jobDefinitionManager.insert(jobDefinitionEntity);
        jobDeclaration.setJobDefinitionId(jobDefinitionEntity.getId());
    }

    protected void adjustStartEventSubscriptions(final ProcessDefinitionEntity newLatestProcessDefinition, final ProcessDefinitionEntity oldLatestProcessDefinition) {
        this.removeObsoleteTimers(newLatestProcessDefinition);
        this.addTimerDeclarations(newLatestProcessDefinition);
        this.removeObsoleteEventSubscriptions(newLatestProcessDefinition, oldLatestProcessDefinition);
        this.addEventSubscriptions(newLatestProcessDefinition);
    }

    protected void addTimerDeclarations(final ProcessDefinitionEntity processDefinition) {
        final List<TimerDeclarationImpl> timerDeclarations = (List<TimerDeclarationImpl>) processDefinition.getProperty("timerStart");
        if (timerDeclarations != null) {
            for (final TimerDeclarationImpl timerDeclaration : timerDeclarations) {
                final String deploymentId = processDefinition.getDeploymentId();
                timerDeclaration.createStartTimerInstance(deploymentId);
            }
        }
    }

    protected void removeObsoleteTimers(final ProcessDefinitionEntity processDefinition) {
        final List<JobEntity> jobsToDelete = this.getJobManager().findJobsByConfiguration("timer-start-event", processDefinition.getKey(), processDefinition.getTenantId());
        for (final JobEntity job : jobsToDelete) {
            new DeleteJobsCmd(job.getId()).execute(Context.getCommandContext());
        }
    }

    protected void removeObsoleteEventSubscriptions(final ProcessDefinitionEntity processDefinition, final ProcessDefinitionEntity latestProcessDefinition) {
        if (latestProcessDefinition != null) {
            final EventSubscriptionManager eventSubscriptionManager = this.getEventSubscriptionManager();
            final List<EventSubscriptionEntity> subscriptionsToDelete = new ArrayList<EventSubscriptionEntity>();
            final List<EventSubscriptionEntity> messageEventSubscriptions = eventSubscriptionManager.findEventSubscriptionsByConfiguration(EventType.MESSAGE.name(), latestProcessDefinition.getId());
            subscriptionsToDelete.addAll(messageEventSubscriptions);
            final List<EventSubscriptionEntity> signalEventSubscriptions = eventSubscriptionManager.findEventSubscriptionsByConfiguration(EventType.SIGNAL.name(), latestProcessDefinition.getId());
            subscriptionsToDelete.addAll(signalEventSubscriptions);
            final List<EventSubscriptionEntity> conditionalEventSubscriptions = eventSubscriptionManager.findEventSubscriptionsByConfiguration(EventType.CONDITONAL.name(), latestProcessDefinition.getId());
            subscriptionsToDelete.addAll(conditionalEventSubscriptions);
            for (final EventSubscriptionEntity eventSubscriptionEntity : subscriptionsToDelete) {
                eventSubscriptionEntity.delete();
            }
        }
    }

    public void addEventSubscriptions(final ProcessDefinitionEntity processDefinition) {
        final Map<String, EventSubscriptionDeclaration> eventDefinitions = processDefinition.getProperties().get(BpmnProperties.EVENT_SUBSCRIPTION_DECLARATIONS);
        for (final EventSubscriptionDeclaration eventDefinition : eventDefinitions.values()) {
            this.addEventSubscription(processDefinition, eventDefinition);
        }
    }

    protected void addEventSubscription(final ProcessDefinitionEntity processDefinition, final EventSubscriptionDeclaration eventDefinition) {
        if (eventDefinition.isStartEvent()) {
            final String eventType = eventDefinition.getEventType();
            if (eventType.equals(EventType.MESSAGE.name())) {
                this.addMessageStartEventSubscription(eventDefinition, processDefinition);
            } else if (eventType.equals(EventType.SIGNAL.name())) {
                this.addSignalStartEventSubscription(eventDefinition, processDefinition);
            } else if (eventType.equals(EventType.CONDITONAL.name())) {
                this.addConditionalStartEventSubscription(eventDefinition, processDefinition);
            }
        }
    }

    protected void addMessageStartEventSubscription(final EventSubscriptionDeclaration messageEventDefinition, final ProcessDefinitionEntity processDefinition) {
        final String tenantId = processDefinition.getTenantId();
        if (this.isSameMessageEventSubscriptionAlreadyPresent(messageEventDefinition, tenantId)) {
            throw BpmnDeployer.LOG.messageEventSubscriptionWithSameNameExists(processDefinition.getResourceName(), messageEventDefinition.getUnresolvedEventName());
        }
        final EventSubscriptionEntity newSubscription = messageEventDefinition.createSubscriptionForStartEvent(processDefinition);
        newSubscription.insert();
    }

    protected boolean isSameMessageEventSubscriptionAlreadyPresent(final EventSubscriptionDeclaration eventSubscription, final String tenantId) {
        List<EventSubscriptionEntity> subscriptionsForSameMessageName = this.getEventSubscriptionManager().findEventSubscriptionsByNameAndTenantId(EventType.MESSAGE.name(), eventSubscription.getUnresolvedEventName(), tenantId);
        final List<EventSubscriptionEntity> cachedSubscriptions = this.getDbEntityManager().getCachedEntitiesByType(EventSubscriptionEntity.class);
        for (final EventSubscriptionEntity cachedSubscription : cachedSubscriptions) {
            if (eventSubscription.getUnresolvedEventName().equals(cachedSubscription.getEventName()) && this.hasTenantId(cachedSubscription, tenantId) && !subscriptionsForSameMessageName.contains(cachedSubscription)) {
                subscriptionsForSameMessageName.add(cachedSubscription);
            }
        }
        subscriptionsForSameMessageName = this.getDbEntityManager().pruneDeletedEntities(subscriptionsForSameMessageName);
        subscriptionsForSameMessageName = this.filterSubscriptionsOfDifferentType(eventSubscription, subscriptionsForSameMessageName);
        return !subscriptionsForSameMessageName.isEmpty();
    }

    protected boolean hasTenantId(final EventSubscriptionEntity cachedSubscription, final String tenantId) {
        if (tenantId == null) {
            return cachedSubscription.getTenantId() == null;
        }
        return tenantId.equals(cachedSubscription.getTenantId());
    }

    protected List<EventSubscriptionEntity> filterSubscriptionsOfDifferentType(final EventSubscriptionDeclaration eventSubscription, final List<EventSubscriptionEntity> subscriptionsForSameMessageName) {
        final ArrayList<EventSubscriptionEntity> filteredSubscriptions = new ArrayList<EventSubscriptionEntity>(subscriptionsForSameMessageName);
        for (final EventSubscriptionEntity subscriptionEntity : new ArrayList<EventSubscriptionEntity>(subscriptionsForSameMessageName)) {
            if (this.isSubscriptionOfDifferentTypeAsDeclaration(subscriptionEntity, eventSubscription)) {
                filteredSubscriptions.remove(subscriptionEntity);
            }
        }
        return filteredSubscriptions;
    }

    protected boolean isSubscriptionOfDifferentTypeAsDeclaration(final EventSubscriptionEntity subscriptionEntity, final EventSubscriptionDeclaration declaration) {
        return (declaration.isStartEvent() && this.isSubscriptionForIntermediateEvent(subscriptionEntity)) || (!declaration.isStartEvent() && this.isSubscriptionForStartEvent(subscriptionEntity));
    }

    protected boolean isSubscriptionForStartEvent(final EventSubscriptionEntity subscriptionEntity) {
        return subscriptionEntity.getExecutionId() == null;
    }

    protected boolean isSubscriptionForIntermediateEvent(final EventSubscriptionEntity subscriptionEntity) {
        return subscriptionEntity.getExecutionId() != null;
    }

    protected void addSignalStartEventSubscription(final EventSubscriptionDeclaration signalEventDefinition, final ProcessDefinitionEntity processDefinition) {
        final EventSubscriptionEntity newSubscription = signalEventDefinition.createSubscriptionForStartEvent(processDefinition);
        newSubscription.insert();
    }

    protected void addConditionalStartEventSubscription(final EventSubscriptionDeclaration conditionalEventDefinition, final ProcessDefinitionEntity processDefinition) {
        final EventSubscriptionEntity newSubscription = conditionalEventDefinition.createSubscriptionForStartEvent(processDefinition);
        newSubscription.insert();
    }

    protected void addAuthorizationsFromIterator(final Set<Expression> exprSet, final ProcessDefinitionEntity processDefinition, final ExprType exprType) {
        if (exprSet != null) {
            for (final Expression expr : exprSet) {
                final IdentityLinkEntity identityLink = new IdentityLinkEntity();
                identityLink.setProcessDef(processDefinition);
                if (exprType.equals(ExprType.USER)) {
                    identityLink.setUserId(expr.toString());
                } else if (exprType.equals(ExprType.GROUP)) {
                    identityLink.setGroupId(expr.toString());
                }
                identityLink.setType("candidate");
                identityLink.setTenantId(processDefinition.getTenantId());
                identityLink.insert();
            }
        }
    }

    protected void addAuthorizations(final ProcessDefinitionEntity processDefinition) {
        this.addAuthorizationsFromIterator(processDefinition.getCandidateStarterUserIdExpressions(), processDefinition, ExprType.USER);
        this.addAuthorizationsFromIterator(processDefinition.getCandidateStarterGroupIdExpressions(), processDefinition, ExprType.GROUP);
    }

    protected DbEntityManager getDbEntityManager() {
        return this.getCommandContext().getDbEntityManager();
    }

    protected JobManager getJobManager() {
        return this.getCommandContext().getJobManager();
    }

    protected JobDefinitionManager getJobDefinitionManager() {
        return this.getCommandContext().getJobDefinitionManager();
    }

    protected EventSubscriptionManager getEventSubscriptionManager() {
        return this.getCommandContext().getEventSubscriptionManager();
    }

    protected ProcessDefinitionManager getProcessDefinitionManager() {
        return this.getCommandContext().getProcessDefinitionManager();
    }

    public ExpressionManager getExpressionManager() {
        return this.expressionManager;
    }

    public void setExpressionManager(final ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }

    public BpmnParser getBpmnParser() {
        return this.bpmnParser;
    }

    public void setBpmnParser(final BpmnParser bpmnParser) {
        this.bpmnParser = bpmnParser;
    }

    static {
        BpmnDeployer.LOG = ProcessEngineLogger.BPMN_PARSE_LOGGER;
        BPMN_RESOURCE_SUFFIXES = new String[]{"bpmn20.xml", "bpmn"};
        JOB_DECLARATIONS_PROPERTY = new PropertyMapKey<String, List<JobDeclaration<?, ?>>>("JOB_DECLARATIONS_PROPERTY");
    }

    enum ExprType {
        USER,
        GROUP;
    }
}
