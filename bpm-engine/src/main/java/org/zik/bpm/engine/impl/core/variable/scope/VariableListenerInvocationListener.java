// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.runtime.VariableInstance;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;

public class VariableListenerInvocationListener implements VariableInstanceLifecycleListener<VariableInstanceEntity>
{
    protected final AbstractVariableScope targetScope;
    
    public VariableListenerInvocationListener(final AbstractVariableScope targetScope) {
        this.targetScope = targetScope;
    }
    
    @Override
    public void onCreate(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        this.handleEvent(new VariableEvent(variable, "create", sourceScope));
    }
    
    @Override
    public void onUpdate(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        this.handleEvent(new VariableEvent(variable, "update", sourceScope));
    }
    
    @Override
    public void onDelete(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        this.handleEvent(new VariableEvent(variable, "delete", sourceScope));
    }
    
    protected void handleEvent(final VariableEvent event) {
        final AbstractVariableScope sourceScope = event.getSourceScope();
        if (sourceScope instanceof ExecutionEntity) {
            this.addEventToScopeExecution((ExecutionEntity)sourceScope, event);
        }
        else if (sourceScope instanceof TaskEntity) {
            final TaskEntity task = (TaskEntity)sourceScope;
            final ExecutionEntity execution = task.getExecution();
            if (execution != null) {
                this.addEventToScopeExecution(execution, event);
            }
        }
        else {
            if (!(sourceScope.getParentVariableScope() instanceof ExecutionEntity)) {
                throw new ProcessEngineException("BPMN execution scope expected");
            }
            this.addEventToScopeExecution((ExecutionEntity)sourceScope.getParentVariableScope(), event);
        }
    }
    
    protected void addEventToScopeExecution(final ExecutionEntity sourceScope, final VariableEvent event) {
        final ExecutionEntity sourceExecution = sourceScope;
        final ExecutionEntity scopeExecution = sourceExecution.isScope() ? sourceExecution : sourceExecution.getParent();
        scopeExecution.delayEvent((PvmExecutionImpl)this.targetScope, event);
    }
}
