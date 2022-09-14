// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;

public class MigratingIncident implements MigratingInstance
{
    protected IncidentEntity incident;
    protected ScopeImpl targetScope;
    protected String targetJobDefinitionId;
    
    public MigratingIncident(final IncidentEntity incident, final ScopeImpl targetScope) {
        this.incident = incident;
        this.targetScope = targetScope;
    }
    
    public void setTargetJobDefinitionId(final String targetJobDefinitionId) {
        this.targetJobDefinitionId = targetJobDefinitionId;
    }
    
    @Override
    public boolean isDetached() {
        return this.incident.getExecutionId() == null;
    }
    
    @Override
    public void detachState() {
        this.incident.setExecution(null);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance newOwningInstance) {
        this.attachTo(newOwningInstance.resolveRepresentativeExecution());
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        this.attachTo(targetTransitionInstance.resolveRepresentativeExecution());
    }
    
    @Override
    public void migrateState() {
        this.incident.setActivityId(this.targetScope.getId());
        this.incident.setProcessDefinitionId(this.targetScope.getProcessDefinition().getId());
        this.incident.setJobDefinitionId(this.targetJobDefinitionId);
        this.migrateHistory();
    }
    
    protected void migrateHistory() {
        final HistoryLevel historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.INCIDENT_MIGRATE, this)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricIncidentMigrateEvt(MigratingIncident.this.incident);
                }
            });
        }
    }
    
    @Override
    public void migrateDependentEntities() {
    }
    
    protected void attachTo(final ExecutionEntity execution) {
        this.incident.setExecution(execution);
    }
}
