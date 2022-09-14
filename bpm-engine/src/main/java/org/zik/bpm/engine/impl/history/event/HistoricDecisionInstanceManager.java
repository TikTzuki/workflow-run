// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Date;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.history.CleanableHistoricDecisionInstanceReportResult;
import org.zik.bpm.engine.impl.CleanableHistoricDecisionInstanceReportImpl;
import org.zik.bpm.engine.impl.variable.serializer.AbstractTypedValueSerializer;
import java.util.Set;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.Collections;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricDecisionInstanceQueryImpl;
import org.zik.bpm.engine.history.HistoricDecisionOutputInstance;
import org.zik.bpm.engine.history.HistoricDecisionInputInstance;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricDecisionInstanceManager extends AbstractHistoricManager
{
    public void deleteHistoricDecisionInstancesByDecisionDefinitionId(final String decisionDefinitionId) {
        if (this.isHistoryEnabled()) {
            final List<HistoricDecisionInstanceEntity> decisionInstances = this.findHistoricDecisionInstancesByDecisionDefinitionId(decisionDefinitionId);
            final List<String> decisionInstanceIds = new ArrayList<String>();
            for (final HistoricDecisionInstanceEntity decisionInstance : decisionInstances) {
                decisionInstanceIds.add(decisionInstance.getId());
                decisionInstance.delete();
            }
            if (!decisionInstanceIds.isEmpty()) {
                this.deleteHistoricDecisionInstanceByIds(decisionInstanceIds);
            }
        }
    }
    
    protected List<HistoricDecisionInstanceEntity> findHistoricDecisionInstancesByDecisionDefinitionId(final String decisionDefinitionId) {
        return (List<HistoricDecisionInstanceEntity>)this.getDbEntityManager().selectList("selectHistoricDecisionInstancesByDecisionDefinitionId", this.configureParameterizedQuery(decisionDefinitionId));
    }
    
    public void deleteHistoricDecisionInstanceByIds(final List<String> decisionInstanceIds) {
        this.getDbEntityManager().deletePreserveOrder(ByteArrayEntity.class, "deleteHistoricDecisionInputInstanceByteArraysByDecisionInstanceIds", decisionInstanceIds);
        this.getDbEntityManager().deletePreserveOrder(ByteArrayEntity.class, "deleteHistoricDecisionOutputInstanceByteArraysByDecisionInstanceIds", decisionInstanceIds);
        this.getDbEntityManager().deletePreserveOrder(HistoricDecisionInputInstanceEntity.class, "deleteHistoricDecisionInputInstanceByDecisionInstanceIds", decisionInstanceIds);
        this.getDbEntityManager().deletePreserveOrder(HistoricDecisionOutputInstanceEntity.class, "deleteHistoricDecisionOutputInstanceByDecisionInstanceIds", decisionInstanceIds);
        this.getDbEntityManager().deletePreserveOrder(HistoricDecisionInstanceEntity.class, "deleteHistoricDecisionInstanceByIds", decisionInstanceIds);
    }
    
    public void insertHistoricDecisionInstances(final HistoricDecisionEvaluationEvent event) {
        if (this.isHistoryEnabled()) {
            final HistoricDecisionInstanceEntity rootHistoricDecisionInstance = event.getRootHistoricDecisionInstance();
            this.insertHistoricDecisionInstance(rootHistoricDecisionInstance);
            for (final HistoricDecisionInstanceEntity requiredHistoricDecisionInstances : event.getRequiredHistoricDecisionInstances()) {
                requiredHistoricDecisionInstances.setRootDecisionInstanceId(rootHistoricDecisionInstance.getId());
                this.insertHistoricDecisionInstance(requiredHistoricDecisionInstances);
            }
        }
    }
    
    protected void insertHistoricDecisionInstance(final HistoricDecisionInstanceEntity historicDecisionInstance) {
        this.getDbEntityManager().insert(historicDecisionInstance);
        this.insertHistoricDecisionInputInstances(historicDecisionInstance.getInputs(), historicDecisionInstance.getId());
        this.insertHistoricDecisionOutputInstances(historicDecisionInstance.getOutputs(), historicDecisionInstance.getId());
    }
    
    protected void insertHistoricDecisionInputInstances(final List<HistoricDecisionInputInstance> inputs, final String decisionInstanceId) {
        for (final HistoricDecisionInputInstance input : inputs) {
            final HistoricDecisionInputInstanceEntity inputEntity = (HistoricDecisionInputInstanceEntity)input;
            inputEntity.setDecisionInstanceId(decisionInstanceId);
            this.getDbEntityManager().insert(inputEntity);
        }
    }
    
    protected void insertHistoricDecisionOutputInstances(final List<HistoricDecisionOutputInstance> outputs, final String decisionInstanceId) {
        for (final HistoricDecisionOutputInstance output : outputs) {
            final HistoricDecisionOutputInstanceEntity outputEntity = (HistoricDecisionOutputInstanceEntity)output;
            outputEntity.setDecisionInstanceId(decisionInstanceId);
            this.getDbEntityManager().insert(outputEntity);
        }
    }
    
    public List<HistoricDecisionInstance> findHistoricDecisionInstancesByQueryCriteria(final HistoricDecisionInstanceQueryImpl query, final Page page) {
        if (this.isHistoryEnabled()) {
            this.configureQuery(query);
            final List<HistoricDecisionInstance> decisionInstances = (List<HistoricDecisionInstance>)this.getDbEntityManager().selectList("selectHistoricDecisionInstancesByQueryCriteria", query, page);
            this.enrichHistoricDecisionsWithInputsAndOutputs(query, decisionInstances);
            return decisionInstances;
        }
        return Collections.emptyList();
    }
    
    public List<ImmutablePair<String, String>> findHistoricDecisionInstanceDeploymentIdMappingsByQueryCriteria(final HistoricDecisionInstanceQueryImpl query) {
        if (this.isHistoryEnabled()) {
            this.configureQuery(query);
            return (List<ImmutablePair<String, String>>)this.getDbEntityManager().selectList("selectHistoricDecisionInstanceDeploymentIdMappingsByQueryCriteria", query);
        }
        return Collections.emptyList();
    }
    
    public void enrichHistoricDecisionsWithInputsAndOutputs(final HistoricDecisionInstanceQueryImpl query, final List<HistoricDecisionInstance> decisionInstances) {
        final Map<String, HistoricDecisionInstanceEntity> decisionInstancesById = new HashMap<String, HistoricDecisionInstanceEntity>();
        for (final HistoricDecisionInstance decisionInstance : decisionInstances) {
            decisionInstancesById.put(decisionInstance.getId(), (HistoricDecisionInstanceEntity)decisionInstance);
        }
        if (!decisionInstances.isEmpty() && query.isIncludeInput()) {
            this.appendHistoricDecisionInputInstances(decisionInstancesById, query);
        }
        if (!decisionInstances.isEmpty() && query.isIncludeOutputs()) {
            this.appendHistoricDecisionOutputInstances(decisionInstancesById, query);
        }
    }
    
    public List<String> findHistoricDecisionInstanceIdsForCleanup(final Integer batchSize, final int minuteFrom, final int minuteTo) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("currentTimestamp", ClockUtil.getCurrentTime());
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        final ListQueryParameterObject parameterObject = new ListQueryParameterObject(parameters, 0, batchSize);
        return (List<String>)this.getDbEntityManager().selectList("selectHistoricDecisionInstanceIdsForCleanup", parameterObject);
    }
    
    protected void appendHistoricDecisionInputInstances(final Map<String, HistoricDecisionInstanceEntity> decisionInstancesById, final HistoricDecisionInstanceQueryImpl query) {
        final List<HistoricDecisionInputInstanceEntity> decisionInputInstances = this.findHistoricDecisionInputInstancesByDecisionInstanceIds(decisionInstancesById.keySet());
        this.initializeInputInstances(decisionInstancesById.values());
        for (final HistoricDecisionInputInstanceEntity decisionInputInstance : decisionInputInstances) {
            final HistoricDecisionInstanceEntity historicDecisionInstance = decisionInstancesById.get(decisionInputInstance.getDecisionInstanceId());
            historicDecisionInstance.addInput(decisionInputInstance);
            if (!this.isBinaryValue(decisionInputInstance) || query.isByteArrayFetchingEnabled()) {
                this.fetchVariableValue(decisionInputInstance, query.isCustomObjectDeserializationEnabled());
            }
        }
    }
    
    protected void initializeInputInstances(final Collection<HistoricDecisionInstanceEntity> decisionInstances) {
        for (final HistoricDecisionInstanceEntity decisionInstance : decisionInstances) {
            decisionInstance.setInputs(new ArrayList<HistoricDecisionInputInstance>());
        }
    }
    
    protected List<HistoricDecisionInputInstanceEntity> findHistoricDecisionInputInstancesByDecisionInstanceIds(final Set<String> historicDecisionInstanceKeys) {
        return (List<HistoricDecisionInputInstanceEntity>)this.getDbEntityManager().selectList("selectHistoricDecisionInputInstancesByDecisionInstanceIds", historicDecisionInstanceKeys);
    }
    
    protected boolean isBinaryValue(final HistoricDecisionInputInstance decisionInputInstance) {
        return AbstractTypedValueSerializer.BINARY_VALUE_TYPES.contains(decisionInputInstance.getTypeName());
    }
    
    protected void fetchVariableValue(final HistoricDecisionInputInstanceEntity decisionInputInstance, final boolean isCustomObjectDeserializationEnabled) {
        try {
            decisionInputInstance.getTypedValue(isCustomObjectDeserializationEnabled);
        }
        catch (Exception t) {
            HistoricDecisionInstanceManager.LOG.failedTofetchVariableValue(t);
        }
    }
    
    protected void appendHistoricDecisionOutputInstances(final Map<String, HistoricDecisionInstanceEntity> decisionInstancesById, final HistoricDecisionInstanceQueryImpl query) {
        final List<HistoricDecisionOutputInstanceEntity> decisionOutputInstances = this.findHistoricDecisionOutputInstancesByDecisionInstanceIds(decisionInstancesById.keySet());
        this.initializeOutputInstances(decisionInstancesById.values());
        for (final HistoricDecisionOutputInstanceEntity decisionOutputInstance : decisionOutputInstances) {
            final HistoricDecisionInstanceEntity historicDecisionInstance = decisionInstancesById.get(decisionOutputInstance.getDecisionInstanceId());
            historicDecisionInstance.addOutput(decisionOutputInstance);
            if (!this.isBinaryValue(decisionOutputInstance) || query.isByteArrayFetchingEnabled()) {
                this.fetchVariableValue(decisionOutputInstance, query.isCustomObjectDeserializationEnabled());
            }
        }
    }
    
    protected void initializeOutputInstances(final Collection<HistoricDecisionInstanceEntity> decisionInstances) {
        for (final HistoricDecisionInstanceEntity decisionInstance : decisionInstances) {
            decisionInstance.setOutputs(new ArrayList<HistoricDecisionOutputInstance>());
        }
    }
    
    protected List<HistoricDecisionOutputInstanceEntity> findHistoricDecisionOutputInstancesByDecisionInstanceIds(final Set<String> decisionInstanceKeys) {
        return (List<HistoricDecisionOutputInstanceEntity>)this.getDbEntityManager().selectList("selectHistoricDecisionOutputInstancesByDecisionInstanceIds", decisionInstanceKeys);
    }
    
    protected boolean isBinaryValue(final HistoricDecisionOutputInstance decisionOutputInstance) {
        return AbstractTypedValueSerializer.BINARY_VALUE_TYPES.contains(decisionOutputInstance.getTypeName());
    }
    
    protected void fetchVariableValue(final HistoricDecisionOutputInstanceEntity decisionOutputInstance, final boolean isCustomObjectDeserializationEnabled) {
        try {
            decisionOutputInstance.getTypedValue(isCustomObjectDeserializationEnabled);
        }
        catch (Exception t) {
            HistoricDecisionInstanceManager.LOG.failedTofetchVariableValue(t);
        }
    }
    
    public HistoricDecisionInstanceEntity findHistoricDecisionInstance(final String historicDecisionInstanceId) {
        if (this.isHistoryEnabled()) {
            return (HistoricDecisionInstanceEntity)this.getDbEntityManager().selectOne("selectHistoricDecisionInstanceByDecisionInstanceId", this.configureParameterizedQuery(historicDecisionInstanceId));
        }
        return null;
    }
    
    public long findHistoricDecisionInstanceCountByQueryCriteria(final HistoricDecisionInstanceQueryImpl query) {
        if (this.isHistoryEnabled()) {
            this.configureQuery(query);
            return (long)this.getDbEntityManager().selectOne("selectHistoricDecisionInstanceCountByQueryCriteria", query);
        }
        return 0L;
    }
    
    public List<HistoricDecisionInstance> findHistoricDecisionInstancesByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<HistoricDecisionInstance>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricDecisionInstancesByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findHistoricDecisionInstanceCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectHistoricDecisionInstanceCountByNativeQuery", parameterMap);
    }
    
    protected void configureQuery(final HistoricDecisionInstanceQueryImpl query) {
        this.getAuthorizationManager().configureHistoricDecisionInstanceQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
    
    public List<CleanableHistoricDecisionInstanceReportResult> findCleanableHistoricDecisionInstancesReportByCriteria(final CleanableHistoricDecisionInstanceReportImpl query, final Page page) {
        query.setCurrentTimestamp(ClockUtil.getCurrentTime());
        this.getAuthorizationManager().configureQueryHistoricFinishedInstanceReport(query, Resources.DECISION_DEFINITION);
        this.getTenantManager().configureQuery(query);
        return (List<CleanableHistoricDecisionInstanceReportResult>)this.getDbEntityManager().selectList("selectFinishedDecisionInstancesReportEntities", query, page);
    }
    
    public long findCleanableHistoricDecisionInstancesReportCountByCriteria(final CleanableHistoricDecisionInstanceReportImpl query) {
        query.setCurrentTimestamp(ClockUtil.getCurrentTime());
        this.getAuthorizationManager().configureQueryHistoricFinishedInstanceReport(query, Resources.DECISION_DEFINITION);
        this.getTenantManager().configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectFinishedDecisionInstancesReportEntitiesCount", query);
    }
    
    public void addRemovalTimeToDecisionsByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionInstanceEntity.class, "updateHistoricDecisionInstancesByRootProcessInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionInputInstanceEntity.class, "updateHistoricDecisionInputInstancesByRootProcessInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionOutputInstanceEntity.class, "updateHistoricDecisionOutputInstancesByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToDecisionsByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionInstanceEntity.class, "updateHistoricDecisionInstancesByProcessInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionInputInstanceEntity.class, "updateHistoricDecisionInputInstancesByProcessInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionOutputInstanceEntity.class, "updateHistoricDecisionOutputInstancesByProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToDecisionsByRootDecisionInstanceId(final String rootInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootDecisionInstanceId", rootInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionInstanceEntity.class, "updateHistoricDecisionInstancesByRootDecisionInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionInputInstanceEntity.class, "updateHistoricDecisionInputInstancesByRootDecisionInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionOutputInstanceEntity.class, "updateHistoricDecisionOutputInstancesByRootDecisionInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateDecisionInputByteArraysByRootDecisionInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateDecisionOutputByteArraysByRootDecisionInstanceId", parameters);
    }
    
    public void addRemovalTimeToDecisionsByDecisionInstanceId(final String instanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("decisionInstanceId", instanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionInstanceEntity.class, "updateHistoricDecisionInstancesByDecisionInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionInputInstanceEntity.class, "updateHistoricDecisionInputInstancesByDecisionInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(HistoricDecisionOutputInstanceEntity.class, "updateHistoricDecisionOutputInstancesByDecisionInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateDecisionInputByteArraysByDecisionInstanceId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateDecisionOutputByteArraysByDecisionInstanceId", parameters);
    }
    
    public Map<Class<? extends DbEntity>, DbOperation> deleteHistoricDecisionsByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        final Map<Class<? extends DbEntity>, DbOperation> deleteOperations = new HashMap<Class<? extends DbEntity>, DbOperation>();
        final DbOperation deleteDecisionInputInstances = this.getDbEntityManager().deletePreserveOrder(HistoricDecisionInputInstanceEntity.class, "deleteHistoricDecisionInputInstancesByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
        deleteOperations.put(HistoricDecisionInputInstanceEntity.class, deleteDecisionInputInstances);
        final DbOperation deleteDecisionOutputInstances = this.getDbEntityManager().deletePreserveOrder(HistoricDecisionOutputInstanceEntity.class, "deleteHistoricDecisionOutputInstancesByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
        deleteOperations.put(HistoricDecisionOutputInstanceEntity.class, deleteDecisionOutputInstances);
        final DbOperation deleteDecisionInstances = this.getDbEntityManager().deletePreserveOrder(HistoricDecisionInstanceEntity.class, "deleteHistoricDecisionInstancesByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
        deleteOperations.put(HistoricDecisionInstanceEntity.class, deleteDecisionInstances);
        return deleteOperations;
    }
}
