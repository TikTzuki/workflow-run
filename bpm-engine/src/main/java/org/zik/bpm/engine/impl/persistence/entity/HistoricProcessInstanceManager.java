// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Date;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.history.CleanableHistoricProcessInstanceReportResult;
import org.zik.bpm.engine.impl.CleanableHistoricProcessInstanceReportImpl;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.context.Context;
import java.util.List;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricProcessInstanceManager extends AbstractHistoricManager
{
    public HistoricProcessInstanceEntity findHistoricProcessInstance(final String processInstanceId) {
        if (this.isHistoryEnabled()) {
            return this.getDbEntityManager().selectById(HistoricProcessInstanceEntity.class, processInstanceId);
        }
        return null;
    }
    
    public HistoricProcessInstanceEventEntity findHistoricProcessInstanceEvent(final String eventId) {
        if (this.isHistoryEnabled()) {
            return this.getDbEntityManager().selectById(HistoricProcessInstanceEventEntity.class, eventId);
        }
        return null;
    }
    
    public void deleteHistoricProcessInstanceByProcessDefinitionId(final String processDefinitionId) {
        if (this.isHistoryEnabled()) {
            final List<String> historicProcessInstanceIds = (List<String>)this.getDbEntityManager().selectList("selectHistoricProcessInstanceIdsByProcessDefinitionId", processDefinitionId);
            if (!historicProcessInstanceIds.isEmpty()) {
                this.deleteHistoricProcessInstanceByIds(historicProcessInstanceIds);
            }
        }
    }
    
    public void deleteHistoricProcessInstanceByIds(final List<String> processInstanceIds) {
        if (this.isHistoryEnabled()) {
            final CommandContext commandContext = Context.getCommandContext();
            final List<List<String>> partitions = CollectionUtil.partition(processInstanceIds, 2000);
            for (final List<String> partition : partitions) {
                commandContext.getHistoricDetailManager().deleteHistoricDetailsByProcessInstanceIds(partition);
                commandContext.getHistoricVariableInstanceManager().deleteHistoricVariableInstanceByProcessInstanceIds(partition);
                commandContext.getCommentManager().deleteCommentsByProcessInstanceIds(partition);
                commandContext.getAttachmentManager().deleteAttachmentsByProcessInstanceIds(partition);
                commandContext.getHistoricTaskInstanceManager().deleteHistoricTaskInstancesByProcessInstanceIds(partition, false);
                commandContext.getHistoricActivityInstanceManager().deleteHistoricActivityInstancesByProcessInstanceIds(partition);
                commandContext.getHistoricIncidentManager().deleteHistoricIncidentsByProcessInstanceIds(partition);
                commandContext.getHistoricJobLogManager().deleteHistoricJobLogsByProcessInstanceIds(partition);
                commandContext.getHistoricExternalTaskLogManager().deleteHistoricExternalTaskLogsByProcessInstanceIds(partition);
                commandContext.getAuthorizationManager().deleteAuthorizationsByResourceIds(Resources.HISTORIC_PROCESS_INSTANCE, partition);
                commandContext.getDbEntityManager().deletePreserveOrder(HistoricProcessInstanceEntity.class, "deleteHistoricProcessInstances", partition);
            }
        }
    }
    
    public long findHistoricProcessInstanceCountByQueryCriteria(final HistoricProcessInstanceQueryImpl historicProcessInstanceQuery) {
        if (this.isHistoryEnabled()) {
            this.configureQuery(historicProcessInstanceQuery);
            return (long)this.getDbEntityManager().selectOne("selectHistoricProcessInstanceCountByQueryCriteria", historicProcessInstanceQuery);
        }
        return 0L;
    }
    
    public List<HistoricProcessInstance> findHistoricProcessInstancesByQueryCriteria(final HistoricProcessInstanceQueryImpl historicProcessInstanceQuery, final Page page) {
        if (this.isHistoryEnabled()) {
            this.configureQuery(historicProcessInstanceQuery);
            return (List<HistoricProcessInstance>)this.getDbEntityManager().selectList("selectHistoricProcessInstancesByQueryCriteria", historicProcessInstanceQuery, page);
        }
        return (List<HistoricProcessInstance>)Collections.EMPTY_LIST;
    }
    
    public List<HistoricProcessInstance> findHistoricProcessInstancesByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<HistoricProcessInstance>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricProcessInstanceByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findHistoricProcessInstanceCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectHistoricProcessInstanceCountByNativeQuery", parameterMap);
    }
    
    protected void configureQuery(final HistoricProcessInstanceQueryImpl query) {
        this.getAuthorizationManager().configureHistoricProcessInstanceQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    public List<String> findHistoricProcessInstanceIdsForCleanup(final Integer batchSize, final int minuteFrom, final int minuteTo) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("currentTimestamp", ClockUtil.getCurrentTime());
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        final ListQueryParameterObject parameterObject = new ListQueryParameterObject(parameters, 0, batchSize);
        return (List<String>)this.getDbEntityManager().selectList("selectHistoricProcessInstanceIdsForCleanup", parameterObject);
    }
    
    public List<String> findHistoricProcessInstanceIds(final HistoricProcessInstanceQueryImpl historicProcessInstanceQuery) {
        this.configureQuery(historicProcessInstanceQuery);
        return (List<String>)this.getDbEntityManager().selectList("selectHistoricProcessInstanceIdsByQueryCriteria", historicProcessInstanceQuery);
    }
    
    public List<ImmutablePair<String, String>> findDeploymentIdMappingsByQueryCriteria(final HistoricProcessInstanceQueryImpl historicProcessInstanceQuery) {
        this.configureQuery(historicProcessInstanceQuery);
        return (List<ImmutablePair<String, String>>)this.getDbEntityManager().selectList("selectHistoricProcessInstanceDeploymentIdMappingsByQueryCriteria", historicProcessInstanceQuery);
    }
    
    public List<CleanableHistoricProcessInstanceReportResult> findCleanableHistoricProcessInstancesReportByCriteria(final CleanableHistoricProcessInstanceReportImpl query, final Page page) {
        query.setCurrentTimestamp(ClockUtil.getCurrentTime());
        this.getAuthorizationManager().configureQueryHistoricFinishedInstanceReport(query, Resources.PROCESS_DEFINITION);
        this.getTenantManager().configureQuery(query);
        return (List<CleanableHistoricProcessInstanceReportResult>)this.getDbEntityManager().selectList("selectFinishedProcessInstancesReportEntities", query, page);
    }
    
    public long findCleanableHistoricProcessInstancesReportCountByCriteria(final CleanableHistoricProcessInstanceReportImpl query) {
        query.setCurrentTimestamp(ClockUtil.getCurrentTime());
        this.getAuthorizationManager().configureQueryHistoricFinishedInstanceReport(query, Resources.PROCESS_DEFINITION);
        this.getTenantManager().configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectFinishedProcessInstancesReportEntitiesCount", query);
    }
    
    public void addRemovalTimeToProcessInstancesByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getHistoricActivityInstanceManager().addRemovalTimeToActivityInstancesByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getHistoricTaskInstanceManager().addRemovalTimeToTaskInstancesByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getHistoricVariableInstanceManager().addRemovalTimeToVariableInstancesByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getHistoricDetailManager().addRemovalTimeToDetailsByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getHistoricIncidentManager().addRemovalTimeToIncidentsByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getHistoricExternalTaskLogManager().addRemovalTimeToExternalTaskLogByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getHistoricJobLogManager().addRemovalTimeToJobLogByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getOperationLogManager().addRemovalTimeToUserOperationLogByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getHistoricIdentityLinkManager().addRemovalTimeToIdentityLinkLogByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getCommentManager().addRemovalTimeToCommentsByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getAttachmentManager().addRemovalTimeToAttachmentsByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        commandContext.getByteArrayManager().addRemovalTimeToByteArraysByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        if (this.isEnableHistoricInstancePermissions()) {
            commandContext.getAuthorizationManager().addRemovalTimeToAuthorizationsByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        }
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricProcessInstanceEventEntity.class, "updateHistoricProcessInstanceEventsByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeById(final String processInstanceId, final Date removalTime) {
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getHistoricActivityInstanceManager().addRemovalTimeToActivityInstancesByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getHistoricTaskInstanceManager().addRemovalTimeToTaskInstancesByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getHistoricVariableInstanceManager().addRemovalTimeToVariableInstancesByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getHistoricDetailManager().addRemovalTimeToDetailsByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getHistoricIncidentManager().addRemovalTimeToIncidentsByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getHistoricExternalTaskLogManager().addRemovalTimeToExternalTaskLogByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getHistoricJobLogManager().addRemovalTimeToJobLogByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getOperationLogManager().addRemovalTimeToUserOperationLogByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getHistoricIdentityLinkManager().addRemovalTimeToIdentityLinkLogByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getCommentManager().addRemovalTimeToCommentsByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getAttachmentManager().addRemovalTimeToAttachmentsByProcessInstanceId(processInstanceId, removalTime);
        commandContext.getByteArrayManager().addRemovalTimeToByteArraysByProcessInstanceId(processInstanceId, removalTime);
        if (this.isEnableHistoricInstancePermissions()) {
            commandContext.getAuthorizationManager().addRemovalTimeToAuthorizationsByProcessInstanceId(processInstanceId, removalTime);
        }
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricProcessInstanceEventEntity.class, "updateHistoricProcessInstanceByProcessInstanceId", parameters);
    }
    
    public Map<Class<? extends DbEntity>, DbOperation> deleteHistoricProcessInstancesByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final CommandContext commandContext = Context.getCommandContext();
        final Map<Class<? extends DbEntity>, DbOperation> deleteOperations = new HashMap<Class<? extends DbEntity>, DbOperation>();
        final DbOperation deleteActivityInstances = commandContext.getHistoricActivityInstanceManager().deleteHistoricActivityInstancesByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteActivityInstances.getEntityType(), deleteActivityInstances);
        final DbOperation deleteTaskInstances = commandContext.getHistoricTaskInstanceManager().deleteHistoricTaskInstancesByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteTaskInstances.getEntityType(), deleteTaskInstances);
        final DbOperation deleteVariableInstances = commandContext.getHistoricVariableInstanceManager().deleteHistoricVariableInstancesByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteVariableInstances.getEntityType(), deleteVariableInstances);
        final DbOperation deleteDetails = commandContext.getHistoricDetailManager().deleteHistoricDetailsByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteDetails.getEntityType(), deleteDetails);
        final DbOperation deleteIncidents = commandContext.getHistoricIncidentManager().deleteHistoricIncidentsByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteIncidents.getEntityType(), deleteIncidents);
        final DbOperation deleteTaskLog = commandContext.getHistoricExternalTaskLogManager().deleteExternalTaskLogByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteTaskLog.getEntityType(), deleteTaskLog);
        final DbOperation deleteJobLog = commandContext.getHistoricJobLogManager().deleteJobLogByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteJobLog.getEntityType(), deleteJobLog);
        final DbOperation deleteOperationLog = commandContext.getOperationLogManager().deleteOperationLogByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteOperationLog.getEntityType(), deleteOperationLog);
        final DbOperation deleteIdentityLinkLog = commandContext.getHistoricIdentityLinkManager().deleteHistoricIdentityLinkLogByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteIdentityLinkLog.getEntityType(), deleteIdentityLinkLog);
        final DbOperation deleteComments = commandContext.getCommentManager().deleteCommentsByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteComments.getEntityType(), deleteComments);
        final DbOperation deleteAttachments = commandContext.getAttachmentManager().deleteAttachmentsByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteAttachments.getEntityType(), deleteAttachments);
        final DbOperation deleteByteArrays = commandContext.getByteArrayManager().deleteByteArraysByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteByteArrays.getEntityType(), deleteByteArrays);
        final DbOperation deleteAuthorizations = commandContext.getAuthorizationManager().deleteAuthorizationsByRemovalTime(removalTime, minuteFrom, minuteTo, batchSize);
        deleteOperations.put(deleteAuthorizations.getEntityType(), deleteAuthorizations);
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        final DbOperation deleteProcessInstances = this.getDbEntityManager().deletePreserveOrder(HistoricProcessInstanceEntity.class, "deleteHistoricProcessInstancesByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
        deleteOperations.put(deleteProcessInstances.getEntityType(), deleteProcessInstances);
        return deleteOperations;
    }
    
    protected boolean isEnableHistoricInstancePermissions() {
        return Context.getProcessEngineConfiguration().isEnableHistoricInstancePermissions();
    }
}
