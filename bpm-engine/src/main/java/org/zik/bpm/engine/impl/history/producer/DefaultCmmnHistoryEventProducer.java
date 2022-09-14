// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.producer;

import org.zik.bpm.engine.impl.history.event.HistoricCaseActivityInstanceEventEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.history.event.HistoricCaseInstanceEventEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;

public class DefaultCmmnHistoryEventProducer implements CmmnHistoryEventProducer
{
    @Override
    public HistoryEvent createCaseInstanceCreateEvt(final DelegateCaseExecution caseExecution) {
        final CaseExecutionEntity caseExecutionEntity = (CaseExecutionEntity)caseExecution;
        final HistoricCaseInstanceEventEntity evt = this.newCaseInstanceEventEntity(caseExecutionEntity);
        this.initCaseInstanceEvent(evt, caseExecutionEntity, HistoryEventTypes.CASE_INSTANCE_CREATE);
        evt.setCreateTime(ClockUtil.getCurrentTime());
        evt.setCreateUserId(Context.getCommandContext().getAuthenticatedUserId());
        final CmmnExecution superCaseExecution = caseExecutionEntity.getSuperCaseExecution();
        if (superCaseExecution != null) {
            evt.setSuperCaseInstanceId(superCaseExecution.getCaseInstanceId());
        }
        final ExecutionEntity superExecution = caseExecutionEntity.getSuperExecution();
        if (superExecution != null) {
            evt.setSuperProcessInstanceId(superExecution.getProcessInstanceId());
        }
        return evt;
    }
    
    @Override
    public HistoryEvent createCaseInstanceUpdateEvt(final DelegateCaseExecution caseExecution) {
        final CaseExecutionEntity caseExecutionEntity = (CaseExecutionEntity)caseExecution;
        final HistoricCaseInstanceEventEntity evt = this.loadCaseInstanceEventEntity(caseExecutionEntity);
        this.initCaseInstanceEvent(evt, caseExecutionEntity, HistoryEventTypes.CASE_INSTANCE_UPDATE);
        return evt;
    }
    
    @Override
    public HistoryEvent createCaseInstanceCloseEvt(final DelegateCaseExecution caseExecution) {
        final CaseExecutionEntity caseExecutionEntity = (CaseExecutionEntity)caseExecution;
        final HistoricCaseInstanceEventEntity evt = this.loadCaseInstanceEventEntity(caseExecutionEntity);
        this.initCaseInstanceEvent(evt, caseExecutionEntity, HistoryEventTypes.CASE_INSTANCE_CLOSE);
        evt.setEndTime(ClockUtil.getCurrentTime());
        if (evt.getStartTime() != null) {
            evt.setDurationInMillis(evt.getEndTime().getTime() - evt.getStartTime().getTime());
        }
        return evt;
    }
    
    @Override
    public HistoryEvent createCaseActivityInstanceCreateEvt(final DelegateCaseExecution caseExecution) {
        final CaseExecutionEntity caseExecutionEntity = (CaseExecutionEntity)caseExecution;
        final HistoricCaseActivityInstanceEventEntity evt = this.newCaseActivityInstanceEventEntity(caseExecutionEntity);
        this.initCaseActivityInstanceEvent(evt, caseExecutionEntity, HistoryEventTypes.CASE_ACTIVITY_INSTANCE_CREATE);
        evt.setCreateTime(ClockUtil.getCurrentTime());
        return evt;
    }
    
    @Override
    public HistoryEvent createCaseActivityInstanceUpdateEvt(final DelegateCaseExecution caseExecution) {
        final CaseExecutionEntity caseExecutionEntity = (CaseExecutionEntity)caseExecution;
        final HistoricCaseActivityInstanceEventEntity evt = this.loadCaseActivityInstanceEventEntity(caseExecutionEntity);
        this.initCaseActivityInstanceEvent(evt, caseExecutionEntity, HistoryEventTypes.CASE_ACTIVITY_INSTANCE_UPDATE);
        if (caseExecutionEntity.getTask() != null) {
            evt.setTaskId(caseExecutionEntity.getTask().getId());
        }
        if (caseExecutionEntity.getSubProcessInstance() != null) {
            evt.setCalledProcessInstanceId(caseExecutionEntity.getSubProcessInstance().getId());
        }
        if (caseExecutionEntity.getSubCaseInstance() != null) {
            evt.setCalledCaseInstanceId(caseExecutionEntity.getSubCaseInstance().getId());
        }
        return evt;
    }
    
    @Override
    public HistoryEvent createCaseActivityInstanceEndEvt(final DelegateCaseExecution caseExecution) {
        final CaseExecutionEntity caseExecutionEntity = (CaseExecutionEntity)caseExecution;
        final HistoricCaseActivityInstanceEventEntity evt = this.loadCaseActivityInstanceEventEntity(caseExecutionEntity);
        this.initCaseActivityInstanceEvent(evt, caseExecutionEntity, HistoryEventTypes.CASE_ACTIVITY_INSTANCE_END);
        evt.setEndTime(ClockUtil.getCurrentTime());
        if (evt.getStartTime() != null) {
            evt.setDurationInMillis(evt.getEndTime().getTime() - evt.getStartTime().getTime());
        }
        return evt;
    }
    
    protected HistoricCaseInstanceEventEntity newCaseInstanceEventEntity(final CaseExecutionEntity caseExecutionEntity) {
        return new HistoricCaseInstanceEventEntity();
    }
    
    protected HistoricCaseInstanceEventEntity loadCaseInstanceEventEntity(final CaseExecutionEntity caseExecutionEntity) {
        return this.newCaseInstanceEventEntity(caseExecutionEntity);
    }
    
    protected void initCaseInstanceEvent(final HistoricCaseInstanceEventEntity evt, final CaseExecutionEntity caseExecutionEntity, final HistoryEventTypes eventType) {
        evt.setId(caseExecutionEntity.getCaseInstanceId());
        evt.setEventType(eventType.getEventName());
        evt.setCaseDefinitionId(caseExecutionEntity.getCaseDefinitionId());
        evt.setCaseInstanceId(caseExecutionEntity.getCaseInstanceId());
        evt.setCaseExecutionId(caseExecutionEntity.getId());
        evt.setBusinessKey(caseExecutionEntity.getBusinessKey());
        evt.setState(caseExecutionEntity.getState());
        evt.setTenantId(caseExecutionEntity.getTenantId());
    }
    
    protected HistoricCaseActivityInstanceEventEntity newCaseActivityInstanceEventEntity(final CaseExecutionEntity caseExecutionEntity) {
        return new HistoricCaseActivityInstanceEventEntity();
    }
    
    protected HistoricCaseActivityInstanceEventEntity loadCaseActivityInstanceEventEntity(final CaseExecutionEntity caseExecutionEntity) {
        return this.newCaseActivityInstanceEventEntity(caseExecutionEntity);
    }
    
    protected void initCaseActivityInstanceEvent(final HistoricCaseActivityInstanceEventEntity evt, final CaseExecutionEntity caseExecutionEntity, final HistoryEventTypes eventType) {
        evt.setId(caseExecutionEntity.getId());
        evt.setParentCaseActivityInstanceId(caseExecutionEntity.getParentId());
        evt.setEventType(eventType.getEventName());
        evt.setCaseDefinitionId(caseExecutionEntity.getCaseDefinitionId());
        evt.setCaseInstanceId(caseExecutionEntity.getCaseInstanceId());
        evt.setCaseExecutionId(caseExecutionEntity.getId());
        evt.setCaseActivityInstanceState(caseExecutionEntity.getState());
        evt.setRequired(caseExecutionEntity.isRequired());
        evt.setCaseActivityId(caseExecutionEntity.getActivityId());
        evt.setCaseActivityName(caseExecutionEntity.getActivityName());
        evt.setCaseActivityType(caseExecutionEntity.getActivityType());
        evt.setTenantId(caseExecutionEntity.getTenantId());
    }
}
