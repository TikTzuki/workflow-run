// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.producer;

import org.zik.bpm.engine.externaltask.ExternalTask;
import org.zik.bpm.engine.task.IdentityLink;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import java.util.List;
import org.zik.bpm.engine.impl.oplog.UserOperationLogContext;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.delegate.DelegateExecution;

public interface HistoryEventProducer
{
    HistoryEvent createProcessInstanceStartEvt(final DelegateExecution p0);
    
    HistoryEvent createProcessInstanceUpdateEvt(final DelegateExecution p0);
    
    HistoryEvent createProcessInstanceMigrateEvt(final DelegateExecution p0);
    
    HistoryEvent createProcessInstanceEndEvt(final DelegateExecution p0);
    
    HistoryEvent createActivityInstanceStartEvt(final DelegateExecution p0);
    
    HistoryEvent createActivityInstanceUpdateEvt(final DelegateExecution p0);
    
    HistoryEvent createActivityInstanceUpdateEvt(final DelegateExecution p0, final DelegateTask p1);
    
    HistoryEvent createActivityInstanceMigrateEvt(final MigratingActivityInstance p0);
    
    HistoryEvent createActivityInstanceEndEvt(final DelegateExecution p0);
    
    HistoryEvent createTaskInstanceCreateEvt(final DelegateTask p0);
    
    HistoryEvent createTaskInstanceUpdateEvt(final DelegateTask p0);
    
    HistoryEvent createTaskInstanceMigrateEvt(final DelegateTask p0);
    
    HistoryEvent createTaskInstanceCompleteEvt(final DelegateTask p0, final String p1);
    
    List<HistoryEvent> createUserOperationLogEvents(final UserOperationLogContext p0);
    
    HistoryEvent createHistoricVariableCreateEvt(final VariableInstanceEntity p0, final VariableScope p1);
    
    HistoryEvent createHistoricVariableUpdateEvt(final VariableInstanceEntity p0, final VariableScope p1);
    
    HistoryEvent createHistoricVariableMigrateEvt(final VariableInstanceEntity p0);
    
    HistoryEvent createHistoricVariableDeleteEvt(final VariableInstanceEntity p0, final VariableScope p1);
    
    HistoryEvent createFormPropertyUpdateEvt(final ExecutionEntity p0, final String p1, final String p2, final String p3);
    
    HistoryEvent createHistoricIncidentCreateEvt(final Incident p0);
    
    HistoryEvent createHistoricIncidentResolveEvt(final Incident p0);
    
    HistoryEvent createHistoricIncidentDeleteEvt(final Incident p0);
    
    HistoryEvent createHistoricIncidentMigrateEvt(final Incident p0);
    
    HistoryEvent createHistoricIncidentUpdateEvt(final Incident p0);
    
    HistoryEvent createHistoricJobLogCreateEvt(final Job p0);
    
    HistoryEvent createHistoricJobLogFailedEvt(final Job p0, final Throwable p1);
    
    HistoryEvent createHistoricJobLogSuccessfulEvt(final Job p0);
    
    HistoryEvent createHistoricJobLogDeleteEvt(final Job p0);
    
    HistoryEvent createBatchStartEvent(final Batch p0);
    
    HistoryEvent createBatchEndEvent(final Batch p0);
    
    HistoryEvent createHistoricIdentityLinkAddEvent(final IdentityLink p0);
    
    HistoryEvent createHistoricIdentityLinkDeleteEvent(final IdentityLink p0);
    
    HistoryEvent createHistoricExternalTaskLogCreatedEvt(final ExternalTask p0);
    
    HistoryEvent createHistoricExternalTaskLogFailedEvt(final ExternalTask p0);
    
    HistoryEvent createHistoricExternalTaskLogSuccessfulEvt(final ExternalTask p0);
    
    HistoryEvent createHistoricExternalTaskLogDeletedEvt(final ExternalTask p0);
}
