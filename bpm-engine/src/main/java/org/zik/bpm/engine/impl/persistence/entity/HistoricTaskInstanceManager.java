// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.zik.bpm.engine.impl.history.event.HistoricTaskInstanceEventEntity;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import java.util.Map;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Collections;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricTaskInstanceQueryImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.context.Context;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricTaskInstanceManager extends AbstractHistoricManager
{
    public void deleteHistoricTaskInstancesByProcessInstanceIds(final List<String> processInstanceIds, final boolean deleteVariableInstances) {
        final CommandContext commandContext = Context.getCommandContext();
        if (deleteVariableInstances) {
            this.getHistoricVariableInstanceManager().deleteHistoricVariableInstancesByTaskProcessInstanceIds(processInstanceIds);
        }
        this.getHistoricDetailManager().deleteHistoricDetailsByTaskProcessInstanceIds(processInstanceIds);
        commandContext.getCommentManager().deleteCommentsByTaskProcessInstanceIds(processInstanceIds);
        this.getAttachmentManager().deleteAttachmentsByTaskProcessInstanceIds(processInstanceIds);
        this.getHistoricIdentityLinkManager().deleteHistoricIdentityLinksLogByTaskProcessInstanceIds(processInstanceIds);
        this.getDbEntityManager().deletePreserveOrder(HistoricTaskInstanceEntity.class, "deleteHistoricTaskInstanceByProcessInstanceIds", processInstanceIds);
    }
    
    public void deleteHistoricTaskInstancesByCaseInstanceIds(final List<String> caseInstanceIds) {
        final CommandContext commandContext = Context.getCommandContext();
        this.getHistoricDetailManager().deleteHistoricDetailsByTaskCaseInstanceIds(caseInstanceIds);
        commandContext.getCommentManager().deleteCommentsByTaskCaseInstanceIds(caseInstanceIds);
        this.getAttachmentManager().deleteAttachmentsByTaskCaseInstanceIds(caseInstanceIds);
        this.getHistoricIdentityLinkManager().deleteHistoricIdentityLinksLogByTaskCaseInstanceIds(caseInstanceIds);
        this.getDbEntityManager().deletePreserveOrder(HistoricTaskInstanceEntity.class, "deleteHistoricTaskInstanceByCaseInstanceIds", caseInstanceIds);
    }
    
    public long findHistoricTaskInstanceCountByQueryCriteria(final HistoricTaskInstanceQueryImpl historicTaskInstanceQuery) {
        if (this.isHistoryEnabled()) {
            this.configureQuery(historicTaskInstanceQuery);
            return (long)this.getDbEntityManager().selectOne("selectHistoricTaskInstanceCountByQueryCriteria", historicTaskInstanceQuery);
        }
        return 0L;
    }
    
    public List<HistoricTaskInstance> findHistoricTaskInstancesByQueryCriteria(final HistoricTaskInstanceQueryImpl historicTaskInstanceQuery, final Page page) {
        if (this.isHistoryEnabled()) {
            this.configureQuery(historicTaskInstanceQuery);
            return (List<HistoricTaskInstance>)this.getDbEntityManager().selectList("selectHistoricTaskInstancesByQueryCriteria", historicTaskInstanceQuery, page);
        }
        return (List<HistoricTaskInstance>)Collections.EMPTY_LIST;
    }
    
    public HistoricTaskInstanceEntity findHistoricTaskInstanceById(final String taskId) {
        EnsureUtil.ensureNotNull("Invalid historic task id", "taskId", taskId);
        if (this.isHistoryEnabled()) {
            return (HistoricTaskInstanceEntity)this.getDbEntityManager().selectOne("selectHistoricTaskInstance", taskId);
        }
        return null;
    }
    
    public void deleteHistoricTaskInstanceById(final String taskId) {
        if (this.isHistoryEnabled()) {
            final HistoricTaskInstanceEntity historicTaskInstance = this.findHistoricTaskInstanceById(taskId);
            if (historicTaskInstance != null) {
                final CommandContext commandContext = Context.getCommandContext();
                commandContext.getHistoricDetailManager().deleteHistoricDetailsByTaskId(taskId);
                commandContext.getHistoricVariableInstanceManager().deleteHistoricVariableInstancesByTaskId(taskId);
                commandContext.getCommentManager().deleteCommentsByTaskId(taskId);
                commandContext.getAttachmentManager().deleteAttachmentsByTaskId(taskId);
                commandContext.getHistoricIdentityLinkManager().deleteHistoricIdentityLinksLogByTaskId(taskId);
                this.deleteAuthorizations(Resources.HISTORIC_TASK, taskId);
                this.getDbEntityManager().delete(historicTaskInstance);
            }
        }
    }
    
    public List<HistoricTaskInstance> findHistoricTaskInstancesByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<HistoricTaskInstance>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricTaskInstanceByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findHistoricTaskInstanceCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectHistoricTaskInstanceCountByNativeQuery", parameterMap);
    }
    
    public void updateHistoricTaskInstance(final TaskEntity taskEntity) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = configuration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.TASK_INSTANCE_UPDATE, taskEntity)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createTaskInstanceUpdateEvt(taskEntity);
                }
            });
        }
    }
    
    public void addRemovalTimeToTaskInstancesByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricTaskInstanceEventEntity.class, "updateHistoricTaskInstancesByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToTaskInstancesByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricTaskInstanceEventEntity.class, "updateHistoricTaskInstancesByProcessInstanceId", parameters);
    }
    
    public void markTaskInstanceEnded(final String taskId, final String deleteReason) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final TaskEntity taskEntity = Context.getCommandContext().getDbEntityManager().selectById(TaskEntity.class, taskId);
        final HistoryLevel historyLevel = configuration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.TASK_INSTANCE_COMPLETE, taskEntity)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createTaskInstanceCompleteEvt(taskEntity, deleteReason);
                }
            });
        }
    }
    
    public void createHistoricTask(final TaskEntity task) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = configuration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.TASK_INSTANCE_CREATE, task)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createTaskInstanceCreateEvt(task);
                }
            });
        }
    }
    
    protected void configureQuery(final HistoricTaskInstanceQueryImpl query) {
        this.getAuthorizationManager().configureHistoricTaskInstanceQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    public DbOperation deleteHistoricTaskInstancesByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(HistoricTaskInstanceEntity.class, "deleteHistoricTaskInstancesByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
}
