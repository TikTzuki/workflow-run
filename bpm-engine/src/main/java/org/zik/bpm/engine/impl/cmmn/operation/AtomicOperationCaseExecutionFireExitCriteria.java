// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public class AtomicOperationCaseExecutionFireExitCriteria implements CmmnAtomicOperation
{
    @Override
    public String getCanonicalName() {
        return "trigger-exit-criteria";
    }
    
    @Override
    public void execute(final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        behavior.fireExitCriteria(execution);
    }
    
    @Override
    public boolean isAsync(final CmmnExecution execution) {
        return false;
    }
}
