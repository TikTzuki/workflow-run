// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.optimize;

import org.zik.bpm.engine.impl.db.CompositePermissionCheck;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.impl.db.PermissionCheckBuilder;
import org.zik.bpm.engine.impl.HistoricDecisionInstanceQueryImpl;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import org.zik.bpm.engine.impl.persistence.entity.HistoricIncidentEntity;
import org.zik.bpm.engine.history.HistoricVariableUpdate;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.impl.persistence.entity.optimize.OptimizeHistoricIdentityLinkLogEntity;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.history.HistoricActivityInstance;
import java.util.Date;
import java.util.Iterator;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class OptimizeManager extends AbstractManager
{
    public void fetchHistoricVariableUpdateByteArrays(final List<String> byteArrayIds) {
        final List<List<String>> partitions = CollectionUtil.partition(byteArrayIds, 2000);
        for (final List<String> partition : partitions) {
            this.getDbEntityManager().selectList("selectByteArrays", partition);
        }
    }
    
    public List<HistoricActivityInstance> getCompletedHistoricActivityInstances(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("finishedAfter", finishedAfter);
        params.put("finishedAt", finishedAt);
        params.put("maxResults", maxResults);
        return (List<HistoricActivityInstance>)this.getDbEntityManager().selectList("selectCompletedHistoricActivityPage", params);
    }
    
    public List<HistoricActivityInstance> getRunningHistoricActivityInstances(final Date startedAfter, final Date startedAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("startedAfter", startedAfter);
        params.put("startedAt", startedAt);
        params.put("maxResults", maxResults);
        return (List<HistoricActivityInstance>)this.getDbEntityManager().selectList("selectRunningHistoricActivityPage", params);
    }
    
    public List<HistoricTaskInstance> getCompletedHistoricTaskInstances(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("finishedAfter", finishedAfter);
        params.put("finishedAt", finishedAt);
        params.put("maxResults", maxResults);
        return (List<HistoricTaskInstance>)this.getDbEntityManager().selectList("selectCompletedHistoricTaskInstancePage", params);
    }
    
    public List<HistoricTaskInstance> getRunningHistoricTaskInstances(final Date startedAfter, final Date startedAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("startedAfter", startedAfter);
        params.put("startedAt", startedAt);
        params.put("maxResults", maxResults);
        return (List<HistoricTaskInstance>)this.getDbEntityManager().selectList("selectRunningHistoricTaskInstancePage", params);
    }
    
    public List<UserOperationLogEntry> getHistoricUserOperationLogs(final Date occurredAfter, final Date occurredAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final String[] operationTypes = { "SuspendJob", "ActivateJob", "SuspendProcessDefinition", "ActivateProcessDefinition", "Suspend", "Activate" };
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("occurredAfter", occurredAfter);
        params.put("occurredAt", occurredAt);
        params.put("operationTypes", operationTypes);
        params.put("maxResults", maxResults);
        return (List<UserOperationLogEntry>)this.getDbEntityManager().selectList("selectHistoricUserOperationLogPage", params);
    }
    
    public List<OptimizeHistoricIdentityLinkLogEntity> getHistoricIdentityLinkLogs(final Date occurredAfter, final Date occurredAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("occurredAfter", occurredAfter);
        params.put("occurredAt", occurredAt);
        params.put("maxResults", maxResults);
        return (List<OptimizeHistoricIdentityLinkLogEntity>)this.getDbEntityManager().selectList("selectHistoricIdentityLinkPage", params);
    }
    
    public List<HistoricProcessInstance> getCompletedHistoricProcessInstances(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("finishedAfter", finishedAfter);
        params.put("finishedAt", finishedAt);
        params.put("maxResults", maxResults);
        return (List<HistoricProcessInstance>)this.getDbEntityManager().selectList("selectCompletedHistoricProcessInstancePage", params);
    }
    
    public List<HistoricProcessInstance> getRunningHistoricProcessInstances(final Date startedAfter, final Date startedAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("startedAfter", startedAfter);
        params.put("startedAt", startedAt);
        params.put("maxResults", maxResults);
        return (List<HistoricProcessInstance>)this.getDbEntityManager().selectList("selectRunningHistoricProcessInstancePage", params);
    }
    
    public List<HistoricVariableUpdate> getHistoricVariableUpdates(final Date occurredAfter, final Date occurredAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("occurredAfter", occurredAfter);
        params.put("occurredAt", occurredAt);
        params.put("maxResults", maxResults);
        return (List<HistoricVariableUpdate>)this.getDbEntityManager().selectList("selectHistoricVariableUpdatePage", params);
    }
    
    public List<HistoricIncidentEntity> getCompletedHistoricIncidents(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("finishedAfter", finishedAfter);
        params.put("finishedAt", finishedAt);
        params.put("maxResults", maxResults);
        return (List<HistoricIncidentEntity>)this.getDbEntityManager().selectList("selectCompletedHistoricIncidentsPage", params);
    }
    
    public List<HistoricIncidentEntity> getOpenHistoricIncidents(final Date createdAfter, final Date createdAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("createdAfter", createdAfter);
        params.put("createdAt", createdAt);
        params.put("maxResults", maxResults);
        return (List<HistoricIncidentEntity>)this.getDbEntityManager().selectList("selectOpenHistoricIncidentsPage", params);
    }
    
    public List<HistoricDecisionInstance> getHistoricDecisionInstances(final Date evaluatedAfter, final Date evaluatedAt, final int maxResults) {
        this.checkIsAuthorizedToReadHistoryAndTenants();
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("evaluatedAfter", evaluatedAfter);
        params.put("evaluatedAt", evaluatedAt);
        params.put("maxResults", maxResults);
        final List<HistoricDecisionInstance> decisionInstances = (List<HistoricDecisionInstance>)this.getDbEntityManager().selectList("selectHistoricDecisionInstancePage", params);
        final HistoricDecisionInstanceQueryImpl query = (HistoricDecisionInstanceQueryImpl)new HistoricDecisionInstanceQueryImpl().disableBinaryFetching().disableCustomObjectDeserialization().includeInputs().includeOutputs();
        final List<List<HistoricDecisionInstance>> partitions = CollectionUtil.partition(decisionInstances, 2000);
        for (final List<HistoricDecisionInstance> partition : partitions) {
            this.getHistoricDecisionInstanceManager().enrichHistoricDecisionsWithInputsAndOutputs(query, partition);
        }
        return decisionInstances;
    }
    
    private void checkIsAuthorizedToReadHistoryAndTenants() {
        final CompositePermissionCheck necessaryPermissionsForOptimize = new PermissionCheckBuilder().conjunctive().atomicCheckForResourceId(Resources.PROCESS_DEFINITION, "*", Permissions.READ_HISTORY).atomicCheckForResourceId(Resources.DECISION_DEFINITION, "*", Permissions.READ_HISTORY).atomicCheckForResourceId(Resources.TENANT, "*", Permissions.READ).build();
        this.getAuthorizationManager().checkAuthorization(necessaryPermissionsForOptimize);
    }
}
