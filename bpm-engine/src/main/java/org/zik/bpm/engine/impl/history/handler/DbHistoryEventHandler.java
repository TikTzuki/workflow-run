// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.handler;

import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.history.event.HistoricScopeInstanceEvent;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionEvaluationEvent;
import org.zik.bpm.engine.impl.history.event.HistoricVariableUpdateEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;

public class DbHistoryEventHandler implements HistoryEventHandler
{
    @Override
    public void handleEvent(final HistoryEvent historyEvent) {
        if (historyEvent instanceof HistoricVariableUpdateEventEntity) {
            this.insertHistoricVariableUpdateEntity((HistoricVariableUpdateEventEntity)historyEvent);
        }
        else if (historyEvent instanceof HistoricDecisionEvaluationEvent) {
            this.insertHistoricDecisionEvaluationEvent((HistoricDecisionEvaluationEvent)historyEvent);
        }
        else {
            this.insertOrUpdate(historyEvent);
        }
    }
    
    @Override
    public void handleEvents(final List<HistoryEvent> historyEvents) {
        for (final HistoryEvent historyEvent : historyEvents) {
            this.handleEvent(historyEvent);
        }
    }
    
    protected void insertOrUpdate(final HistoryEvent historyEvent) {
        final DbEntityManager dbEntityManager = this.getDbEntityManager();
        if (this.isInitialEvent(historyEvent)) {
            dbEntityManager.insert(historyEvent);
        }
        else if (dbEntityManager.getCachedEntity(historyEvent.getClass(), historyEvent.getId()) == null) {
            if (historyEvent instanceof HistoricScopeInstanceEvent) {
                final HistoricScopeInstanceEvent existingEvent = dbEntityManager.selectById(historyEvent.getClass(), historyEvent.getId());
                if (existingEvent != null) {
                    final HistoricScopeInstanceEvent historicScopeInstanceEvent = (HistoricScopeInstanceEvent)historyEvent;
                    historicScopeInstanceEvent.setStartTime(existingEvent.getStartTime());
                }
            }
            if (historyEvent.getId() != null) {
                dbEntityManager.merge(historyEvent);
            }
        }
    }
    
    protected void insertHistoricVariableUpdateEntity(final HistoricVariableUpdateEventEntity historyEvent) {
        final DbEntityManager dbEntityManager = this.getDbEntityManager();
        if (this.shouldWriteHistoricDetail(historyEvent)) {
            final byte[] byteValue = historyEvent.getByteValue();
            if (byteValue != null) {
                final ByteArrayEntity byteArrayEntity = new ByteArrayEntity(historyEvent.getVariableName(), byteValue, ResourceTypes.HISTORY);
                byteArrayEntity.setRootProcessInstanceId(historyEvent.getRootProcessInstanceId());
                byteArrayEntity.setRemovalTime(historyEvent.getRemovalTime());
                Context.getCommandContext().getByteArrayManager().insertByteArray(byteArrayEntity);
                historyEvent.setByteArrayId(byteArrayEntity.getId());
            }
            dbEntityManager.insert(historyEvent);
        }
        if (historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_CREATE)) {
            final HistoricVariableInstanceEntity persistentObject = new HistoricVariableInstanceEntity(historyEvent);
            dbEntityManager.insert(persistentObject);
        }
        else if (historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_UPDATE) || historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_MIGRATE)) {
            final HistoricVariableInstanceEntity historicVariableInstanceEntity = dbEntityManager.selectById(HistoricVariableInstanceEntity.class, historyEvent.getVariableInstanceId());
            if (historicVariableInstanceEntity != null) {
                historicVariableInstanceEntity.updateFromEvent(historyEvent);
                historicVariableInstanceEntity.setState("CREATED");
            }
            else {
                final HistoricVariableInstanceEntity persistentObject2 = new HistoricVariableInstanceEntity(historyEvent);
                dbEntityManager.insert(persistentObject2);
            }
        }
        else if (historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_DELETE)) {
            final HistoricVariableInstanceEntity historicVariableInstanceEntity = dbEntityManager.selectById(HistoricVariableInstanceEntity.class, historyEvent.getVariableInstanceId());
            if (historicVariableInstanceEntity != null) {
                historicVariableInstanceEntity.setState("DELETED");
            }
        }
    }
    
    protected boolean shouldWriteHistoricDetail(final HistoricVariableUpdateEventEntity historyEvent) {
        return Context.getProcessEngineConfiguration().getHistoryLevel().isHistoryEventProduced(HistoryEventTypes.VARIABLE_INSTANCE_UPDATE_DETAIL, historyEvent) && !historyEvent.isEventOfType(HistoryEventTypes.VARIABLE_INSTANCE_MIGRATE);
    }
    
    protected void insertHistoricDecisionEvaluationEvent(final HistoricDecisionEvaluationEvent event) {
        Context.getCommandContext().getHistoricDecisionInstanceManager().insertHistoricDecisionInstances(event);
    }
    
    protected boolean isInitialEvent(final HistoryEvent historyEvent) {
        return historyEvent.getEventType() == null || historyEvent.isEventOfType(HistoryEventTypes.ACTIVITY_INSTANCE_START) || historyEvent.isEventOfType(HistoryEventTypes.PROCESS_INSTANCE_START) || historyEvent.isEventOfType(HistoryEventTypes.TASK_INSTANCE_CREATE) || historyEvent.isEventOfType(HistoryEventTypes.FORM_PROPERTY_UPDATE) || historyEvent.isEventOfType(HistoryEventTypes.INCIDENT_CREATE) || historyEvent.isEventOfType(HistoryEventTypes.CASE_INSTANCE_CREATE) || historyEvent.isEventOfType(HistoryEventTypes.DMN_DECISION_EVALUATE) || historyEvent.isEventOfType(HistoryEventTypes.BATCH_START) || historyEvent.isEventOfType(HistoryEventTypes.IDENTITY_LINK_ADD) || historyEvent.isEventOfType(HistoryEventTypes.IDENTITY_LINK_DELETE);
    }
    
    protected DbEntityManager getDbEntityManager() {
        return Context.getCommandContext().getDbEntityManager();
    }
}
