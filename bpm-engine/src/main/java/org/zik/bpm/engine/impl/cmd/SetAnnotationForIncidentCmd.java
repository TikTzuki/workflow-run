// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.impl.history.event.HistoricIncidentEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetAnnotationForIncidentCmd implements Command<Void>
{
    protected String incidentId;
    protected String annotation;
    
    public SetAnnotationForIncidentCmd(final String incidentId, final String annotation) {
        this.incidentId = incidentId;
        this.annotation = annotation;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(NotValidException.class, "incident id", (Object)this.incidentId);
        final IncidentEntity incident = (IncidentEntity)commandContext.getIncidentManager().findIncidentById(this.incidentId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "incident", incident);
        if (incident.getExecutionId() != null) {
            final ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(incident.getExecutionId());
            if (execution != null) {
                for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                    checker.checkUpdateProcessInstance(execution);
                }
            }
        }
        incident.setAnnotation(this.annotation);
        this.triggerHistoryEvent(commandContext, incident);
        if (this.annotation == null) {
            commandContext.getOperationLogManager().logClearIncidentAnnotationOperation(this.incidentId);
        }
        else {
            commandContext.getOperationLogManager().logSetIncidentAnnotationOperation(this.incidentId);
        }
        return null;
    }
    
    protected void triggerHistoryEvent(final CommandContext commandContext, final IncidentEntity incident) {
        final HistoryLevel historyLevel = commandContext.getProcessEngineConfiguration().getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.INCIDENT_UPDATE, incident)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    final HistoricIncidentEventEntity incidentUpdateEvt = (HistoricIncidentEventEntity)producer.createHistoricIncidentUpdateEvt(incident);
                    incidentUpdateEvt.setAnnotation(SetAnnotationForIncidentCmd.this.annotation);
                    return incidentUpdateEvt;
                }
            });
        }
    }
}
