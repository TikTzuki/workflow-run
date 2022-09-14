// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public class TaskActivityBehavior extends StageOrTaskActivityBehavior
{
    @Override
    public void onReactivation(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.FAILED, CaseExecutionState.ACTIVE, "re-activate");
    }
    
    @Override
    protected void performStart(final CmmnActivityExecution execution) {
        execution.complete();
    }
    
    @Override
    public void fireExitCriteria(final CmmnActivityExecution execution) {
        execution.exit();
    }
    
    protected boolean isBlocking(final CmmnActivityExecution execution) {
        final CmmnActivity activity = execution.getActivity();
        final Object isBlockingProperty = activity.getProperty("isBlocking");
        return isBlockingProperty != null && isBlockingProperty instanceof Boolean && (boolean)isBlockingProperty;
    }
    
    @Override
    protected String getTypeName() {
        return "task";
    }
}
