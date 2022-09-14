// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public class MilestoneActivityBehavior extends EventListenerOrMilestoneActivityBehavior
{
    @Override
    protected void creating(final CmmnActivityExecution execution) {
        this.evaluateRequiredRule(execution);
    }
    
    @Override
    public void created(final CmmnActivityExecution execution) {
        if (execution.isAvailable() && this.isAtLeastOneEntryCriterionSatisfied(execution)) {
            this.fireEntryCriteria(execution);
        }
    }
    
    @Override
    public void fireEntryCriteria(final CmmnActivityExecution execution) {
        execution.occur();
    }
    
    @Override
    protected String getTypeName() {
        return "milestone";
    }
}
