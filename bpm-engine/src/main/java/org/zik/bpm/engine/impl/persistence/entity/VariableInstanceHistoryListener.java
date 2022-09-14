// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceLifecycleListener;

public class VariableInstanceHistoryListener implements VariableInstanceLifecycleListener<VariableInstanceEntity>
{
    public static final VariableInstanceHistoryListener INSTANCE;
    
    @Override
    public void onCreate(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
        if (this.getHistoryLevel().isHistoryEventProduced(HistoryEventTypes.VARIABLE_INSTANCE_CREATE, variableInstance) && !variableInstance.isTransient()) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricVariableCreateEvt(variableInstance, sourceScope);
                }
            });
        }
    }
    
    @Override
    public void onDelete(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
        if (this.getHistoryLevel().isHistoryEventProduced(HistoryEventTypes.VARIABLE_INSTANCE_DELETE, variableInstance) && !variableInstance.isTransient()) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricVariableDeleteEvt(variableInstance, sourceScope);
                }
            });
        }
    }
    
    @Override
    public void onUpdate(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
        if (this.getHistoryLevel().isHistoryEventProduced(HistoryEventTypes.VARIABLE_INSTANCE_UPDATE, variableInstance) && !variableInstance.isTransient()) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricVariableUpdateEvt(variableInstance, sourceScope);
                }
            });
        }
    }
    
    protected HistoryLevel getHistoryLevel() {
        return Context.getProcessEngineConfiguration().getHistoryLevel();
    }
    
    static {
        INSTANCE = new VariableInstanceHistoryListener();
    }
}
