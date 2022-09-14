// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;

public class MigratingVariableInstance implements MigratingInstance
{
    protected VariableInstanceEntity variable;
    protected boolean isConcurrentLocalInParentScope;
    
    public MigratingVariableInstance(final VariableInstanceEntity variable, final boolean isConcurrentLocalInParentScope) {
        this.variable = variable;
        this.isConcurrentLocalInParentScope = isConcurrentLocalInParentScope;
    }
    
    @Override
    public boolean isDetached() {
        return this.variable.getExecutionId() == null;
    }
    
    @Override
    public void detachState() {
        this.variable.getExecution().removeVariableInternal(this.variable);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance owningActivityInstance) {
        final ExecutionEntity representativeExecution = owningActivityInstance.resolveRepresentativeExecution();
        final ScopeImpl currentScope = owningActivityInstance.getCurrentScope();
        ExecutionEntity newOwningExecution = representativeExecution;
        if (currentScope.isScope() && this.isConcurrentLocalInParentScope) {
            newOwningExecution = representativeExecution.getParent();
        }
        newOwningExecution.addVariableInternal(this.variable);
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance owningActivityInstance) {
        final ExecutionEntity representativeExecution = owningActivityInstance.resolveRepresentativeExecution();
        representativeExecution.addVariableInternal(this.variable);
    }
    
    @Override
    public void migrateState() {
        this.migrateHistory();
    }
    
    protected void migrateHistory() {
        final HistoryLevel historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.VARIABLE_INSTANCE_MIGRATE, this)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricVariableMigrateEvt(MigratingVariableInstance.this.variable);
                }
            });
        }
    }
    
    @Override
    public void migrateDependentEntities() {
    }
    
    public String getVariableName() {
        return this.variable.getName();
    }
}
