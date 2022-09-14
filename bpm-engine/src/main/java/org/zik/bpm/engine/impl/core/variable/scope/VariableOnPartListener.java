// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.camunda.bpm.model.cmmn.VariableTransition;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;

public class VariableOnPartListener implements VariableInstanceLifecycleListener<VariableInstanceEntity>
{
    protected CmmnExecution execution;
    
    public VariableOnPartListener(final CmmnExecution execution) {
        this.execution = execution;
    }
    
    @Override
    public void onCreate(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        this.execution.handleVariableTransition(variable.getName(), VariableTransition.create.name());
    }
    
    @Override
    public void onUpdate(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        this.execution.handleVariableTransition(variable.getName(), VariableTransition.update.name());
    }
    
    @Override
    public void onDelete(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        this.execution.handleVariableTransition(variable.getName(), VariableTransition.delete.name());
    }
}
