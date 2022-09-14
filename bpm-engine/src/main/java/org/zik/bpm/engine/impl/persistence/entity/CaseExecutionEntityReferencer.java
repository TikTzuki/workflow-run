// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.core.variable.scope.VariableStore;

public class CaseExecutionEntityReferencer implements VariableStore.VariableStoreObserver<VariableInstanceEntity>
{
    protected CaseExecutionEntity caseExecution;
    
    public CaseExecutionEntityReferencer(final CaseExecutionEntity caeExecution) {
        this.caseExecution = caeExecution;
    }
    
    @Override
    public void onAdd(final VariableInstanceEntity variable) {
        variable.setCaseExecution(this.caseExecution);
    }
    
    @Override
    public void onRemove(final VariableInstanceEntity variable) {
        variable.setCaseExecution(null);
    }
}
