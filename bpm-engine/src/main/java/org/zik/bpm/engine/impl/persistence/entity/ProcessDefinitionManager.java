// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import java.util.Collection;
import org.zik.bpm.engine.impl.event.EventType;
import java.util.ArrayList;
import org.zik.bpm.engine.runtime.Job;
import java.util.Iterator;
import java.util.Set;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.ProcessDefinitionQueryImpl;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class ProcessDefinitionManager extends AbstractManager implements AbstractResourceDefinitionManager<ProcessDefinitionEntity>
{
    protected static final EnginePersistenceLogger LOG;
    
    public void insertProcessDefinition(final ProcessDefinitionEntity processDefinition) {
        this.getDbEntityManager().insert(processDefinition);
        this.createDefaultAuthorizations(processDefinition);
    }
    
    public ProcessDefinitionEntity findLatestProcessDefinitionByKey(final String processDefinitionKey) {
        final List<ProcessDefinitionEntity> processDefinitions = this.findLatestProcessDefinitionsByKey(processDefinitionKey);
        if (processDefinitions.isEmpty()) {
            return null;
        }
        if (processDefinitions.size() == 1) {
            return processDefinitions.iterator().next();
        }
        throw ProcessDefinitionManager.LOG.multipleTenantsForProcessDefinitionKeyException(processDefinitionKey);
    }
    
    public List<ProcessDefinitionEntity> findLatestProcessDefinitionsByKey(final String processDefinitionKey) {
        return (List<ProcessDefinitionEntity>)this.getDbEntityManager().selectList("selectLatestProcessDefinitionByKey", this.configureParameterizedQuery(processDefinitionKey));
    }
    
    public ProcessDefinitionEntity findLatestProcessDefinitionByKeyAndTenantId(final String processDefinitionKey, final String tenantId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("tenantId", tenantId);
        if (tenantId == null) {
            return (ProcessDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestProcessDefinitionByKeyWithoutTenantId", parameters);
        }
        return (ProcessDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestProcessDefinitionByKeyAndTenantId", parameters);
    }
    
    public ProcessDefinitionEntity findLatestProcessDefinitionById(final String processDefinitionId) {
        return this.getDbEntityManager().selectById(ProcessDefinitionEntity.class, processDefinitionId);
    }
    
    public List<ProcessDefinition> findProcessDefinitionsByQueryCriteria(final ProcessDefinitionQueryImpl processDefinitionQuery, final Page page) {
        this.configureProcessDefinitionQuery(processDefinitionQuery);
        return (List<ProcessDefinition>)this.getDbEntityManager().selectList("selectProcessDefinitionsByQueryCriteria", processDefinitionQuery, page);
    }
    
    public long findProcessDefinitionCountByQueryCriteria(final ProcessDefinitionQueryImpl processDefinitionQuery) {
        this.configureProcessDefinitionQuery(processDefinitionQuery);
        return (long)this.getDbEntityManager().selectOne("selectProcessDefinitionCountByQueryCriteria", processDefinitionQuery);
    }
    
    public ProcessDefinitionEntity findProcessDefinitionByDeploymentAndKey(final String deploymentId, final String processDefinitionKey) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("deploymentId", deploymentId);
        parameters.put("processDefinitionKey", processDefinitionKey);
        return (ProcessDefinitionEntity)this.getDbEntityManager().selectOne("selectProcessDefinitionByDeploymentAndKey", parameters);
    }
    
    public ProcessDefinitionEntity findProcessDefinitionByKeyVersionAndTenantId(final String processDefinitionKey, final Integer processDefinitionVersion, final String tenantId) {
        return this.findProcessDefinitionByKeyVersionOrVersionTag(processDefinitionKey, processDefinitionVersion, null, tenantId);
    }
    
    public ProcessDefinitionEntity findProcessDefinitionByKeyVersionTagAndTenantId(final String processDefinitionKey, final String processDefinitionVersionTag, final String tenantId) {
        return this.findProcessDefinitionByKeyVersionOrVersionTag(processDefinitionKey, null, processDefinitionVersionTag, tenantId);
    }
    
    protected ProcessDefinitionEntity findProcessDefinitionByKeyVersionOrVersionTag(final String processDefinitionKey, final Integer processDefinitionVersion, final String processDefinitionVersionTag, final String tenantId) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        if (processDefinitionVersion != null) {
            parameters.put("processDefinitionVersion", processDefinitionVersion);
        }
        else if (processDefinitionVersionTag != null) {
            parameters.put("processDefinitionVersionTag", processDefinitionVersionTag);
        }
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("tenantId", tenantId);
        final List<ProcessDefinitionEntity> results = (List<ProcessDefinitionEntity>)this.getDbEntityManager().selectList("selectProcessDefinitionByKeyVersionAndTenantId", parameters);
        if (results.size() == 1) {
            return results.get(0);
        }
        if (results.size() > 1) {
            if (processDefinitionVersion != null) {
                throw ProcessDefinitionManager.LOG.toManyProcessDefinitionsException(results.size(), processDefinitionKey, "version", processDefinitionVersion.toString(), tenantId);
            }
            if (processDefinitionVersionTag != null) {
                throw ProcessDefinitionManager.LOG.toManyProcessDefinitionsException(results.size(), processDefinitionKey, "versionTag", processDefinitionVersionTag, tenantId);
            }
        }
        return null;
    }
    
    public List<ProcessDefinition> findProcessDefinitionsByKey(final String processDefinitionKey) {
        final ProcessDefinitionQueryImpl processDefinitionQuery = new ProcessDefinitionQueryImpl().processDefinitionKey(processDefinitionKey);
        return this.findProcessDefinitionsByQueryCriteria(processDefinitionQuery, null);
    }
    
    public List<ProcessDefinition> findProcessDefinitionsStartableByUser(final String user) {
        return ((AbstractQuery<T, ProcessDefinition>)new ProcessDefinitionQueryImpl().startableByUser(user)).list();
    }
    
    public String findPreviousProcessDefinitionId(final String processDefinitionKey, final Integer version, final String tenantId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", processDefinitionKey);
        params.put("version", version);
        params.put("tenantId", tenantId);
        return (String)this.getDbEntityManager().selectOne("selectPreviousProcessDefinitionId", params);
    }
    
    public List<ProcessDefinition> findProcessDefinitionsByDeploymentId(final String deploymentId) {
        return (List<ProcessDefinition>)this.getDbEntityManager().selectList("selectProcessDefinitionByDeploymentId", deploymentId);
    }
    
    public List<ProcessDefinition> findProcessDefinitionsByKeyIn(final String... keys) {
        return (List<ProcessDefinition>)this.getDbEntityManager().selectList("selectProcessDefinitionByKeyIn", keys);
    }
    
    public List<ProcessDefinition> findDefinitionsByKeyAndTenantId(final String processDefinitionKey, final String tenantId, final boolean isTenantIdSet) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isTenantIdSet", isTenantIdSet);
        parameters.put("tenantId", tenantId);
        return (List<ProcessDefinition>)this.getDbEntityManager().selectList("selectProcessDefinitions", parameters);
    }
    
    public List<ProcessDefinition> findDefinitionsByIds(final Set<String> processDefinitionIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionIds", processDefinitionIds);
        parameters.put("isTenantIdSet", false);
        return (List<ProcessDefinition>)this.getDbEntityManager().selectList("selectProcessDefinitions", parameters);
    }
    
    public void updateProcessDefinitionSuspensionStateById(final String processDefinitionId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionId", processDefinitionId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(ProcessDefinitionEntity.class, "updateProcessDefinitionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateProcessDefinitionSuspensionStateByKey(final String processDefinitionKey, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isTenantIdSet", false);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(ProcessDefinitionEntity.class, "updateProcessDefinitionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateProcessDefinitionSuspensionStateByKeyAndTenantId(final String processDefinitionKey, final String tenantId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isTenantIdSet", true);
        parameters.put("tenantId", tenantId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(ProcessDefinitionEntity.class, "updateProcessDefinitionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    protected void cascadeDeleteProcessInstancesForProcessDefinition(final String processDefinitionId, final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.getProcessInstanceManager().deleteProcessInstancesByProcessDefinition(processDefinitionId, "deleted process definition", true, skipCustomListeners, skipIoMappings);
    }
    
    protected void cascadeDeleteHistoryForProcessDefinition(final String processDefinitionId) {
        this.getHistoricIncidentManager().deleteHistoricIncidentsByProcessDefinitionId(processDefinitionId);
        this.getHistoricIdentityLinkManager().deleteHistoricIdentityLinksLogByProcessDefinitionId(processDefinitionId);
        this.getHistoricJobLogManager().deleteHistoricJobLogsByProcessDefinitionId(processDefinitionId);
    }
    
    protected void deleteTimerStartEventsForProcessDefinition(final ProcessDefinition processDefinition) {
        final List<JobEntity> timerStartJobs = this.getJobManager().findJobsByConfiguration("timer-start-event", processDefinition.getKey(), processDefinition.getTenantId());
        final ProcessDefinitionEntity latestVersion = this.getProcessDefinitionManager().findLatestProcessDefinitionByKeyAndTenantId(processDefinition.getKey(), processDefinition.getTenantId());
        if (latestVersion != null && latestVersion.getId().equals(processDefinition.getId())) {
            for (final Job job : timerStartJobs) {
                ((JobEntity)job).delete();
            }
        }
    }
    
    public void deleteSubscriptionsForProcessDefinition(final String processDefinitionId) {
        final List<EventSubscriptionEntity> eventSubscriptionsToRemove = new ArrayList<EventSubscriptionEntity>();
        final List<EventSubscriptionEntity> messageEventSubscriptions = this.getEventSubscriptionManager().findEventSubscriptionsByConfiguration(EventType.MESSAGE.name(), processDefinitionId);
        eventSubscriptionsToRemove.addAll(messageEventSubscriptions);
        final List<EventSubscriptionEntity> signalEventSubscriptions = this.getEventSubscriptionManager().findEventSubscriptionsByConfiguration(EventType.SIGNAL.name(), processDefinitionId);
        eventSubscriptionsToRemove.addAll(signalEventSubscriptions);
        final List<EventSubscriptionEntity> conditionalEventSubscriptions = this.getEventSubscriptionManager().findEventSubscriptionsByConfiguration(EventType.CONDITONAL.name(), processDefinitionId);
        eventSubscriptionsToRemove.addAll(conditionalEventSubscriptions);
        for (final EventSubscriptionEntity eventSubscriptionEntity : eventSubscriptionsToRemove) {
            eventSubscriptionEntity.delete();
        }
    }
    
    public void deleteProcessDefinition(final ProcessDefinition processDefinition, final String processDefinitionId, final boolean cascadeToHistory, final boolean cascadeToInstances, final boolean skipCustomListeners, final boolean skipIoMappings) {
        if (cascadeToHistory) {
            this.cascadeDeleteHistoryForProcessDefinition(processDefinitionId);
            if (cascadeToInstances) {
                this.cascadeDeleteProcessInstancesForProcessDefinition(processDefinitionId, skipCustomListeners, skipIoMappings);
            }
        }
        else {
            final ProcessInstanceQueryImpl procInstQuery = new ProcessInstanceQueryImpl().processDefinitionId(processDefinitionId);
            final long processInstanceCount = this.getProcessInstanceManager().findProcessInstanceCountByQueryCriteria(procInstQuery);
            if (processInstanceCount != 0L) {
                throw ProcessDefinitionManager.LOG.deleteProcessDefinitionWithProcessInstancesException(processDefinitionId, processInstanceCount);
            }
        }
        this.getIdentityLinkManager().deleteIdentityLinksByProcDef(processDefinitionId);
        this.deleteTimerStartEventsForProcessDefinition(processDefinition);
        this.getDbEntityManager().delete(ProcessDefinitionEntity.class, "deleteProcessDefinitionsById", processDefinitionId);
        Context.getProcessEngineConfiguration().getDeploymentCache().removeProcessDefinition(processDefinitionId);
        this.deleteSubscriptionsForProcessDefinition(processDefinitionId);
        this.getJobDefinitionManager().deleteJobDefinitionsByProcessDefinitionId(processDefinition.getId());
    }
    
    protected void createDefaultAuthorizations(final ProcessDefinition processDefinition) {
        if (this.isAuthorizationEnabled()) {
            final ResourceAuthorizationProvider provider = this.getResourceAuthorizationProvider();
            final AuthorizationEntity[] authorizations = provider.newProcessDefinition(processDefinition);
            this.saveDefaultAuthorizations(authorizations);
        }
    }
    
    protected void configureProcessDefinitionQuery(final ProcessDefinitionQueryImpl query) {
        this.getAuthorizationManager().configureProcessDefinitionQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
    
    @Override
    public ProcessDefinitionEntity findLatestDefinitionByKey(final String key) {
        return this.findLatestProcessDefinitionByKey(key);
    }
    
    @Override
    public ProcessDefinitionEntity findLatestDefinitionById(final String id) {
        return this.findLatestProcessDefinitionById(id);
    }
    
    @Override
    public ProcessDefinitionEntity getCachedResourceDefinitionEntity(final String definitionId) {
        return this.getDbEntityManager().getCachedEntity(ProcessDefinitionEntity.class, definitionId);
    }
    
    @Override
    public ProcessDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        return this.findLatestProcessDefinitionByKeyAndTenantId(definitionKey, tenantId);
    }
    
    @Override
    public ProcessDefinitionEntity findDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId) {
        return this.findProcessDefinitionByKeyVersionAndTenantId(definitionKey, definitionVersion, tenantId);
    }
    
    @Override
    public ProcessDefinitionEntity findDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId) {
        return this.findProcessDefinitionByKeyVersionTagAndTenantId(definitionKey, definitionVersionTag, tenantId);
    }
    
    @Override
    public ProcessDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        return this.findProcessDefinitionByDeploymentAndKey(deploymentId, definitionKey);
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
