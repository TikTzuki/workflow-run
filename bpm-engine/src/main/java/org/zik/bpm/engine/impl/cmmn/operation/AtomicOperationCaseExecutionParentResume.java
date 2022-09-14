// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;

public class AtomicOperationCaseExecutionParentResume extends AbstractAtomicOperationCaseExecutionResume
{
    @Override
    public String getCanonicalName() {
        return "case-execution-parent-resume";
    }
    
    @Override
    protected String getEventName() {
        return "parentResume";
    }
    
    @Override
    protected void triggerBehavior(final CmmnActivityBehavior behavior, final CmmnExecution execution) {
        behavior.onParentResume(execution);
    }
}
