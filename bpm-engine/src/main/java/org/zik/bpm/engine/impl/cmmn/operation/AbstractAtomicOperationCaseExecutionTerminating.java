// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public abstract class AbstractAtomicOperationCaseExecutionTerminating implements CmmnAtomicOperation
{
    @Override
    public void execute(final CmmnExecution execution) {
        execution.setCurrentState(this.getTerminatingState());
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        this.triggerBehavior(behavior, execution);
    }
    
    @Override
    public boolean isAsync(final CmmnExecution execution) {
        return false;
    }
    
    protected abstract CaseExecutionState getTerminatingState();
    
    protected abstract void triggerBehavior(final CmmnActivityBehavior p0, final CmmnExecution p1);
}
