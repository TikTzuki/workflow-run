// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class TaskActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected String activityInstanceId;
    
    protected void preExecution(final ActivityExecution execution) throws Exception {
        this.activityInstanceId = execution.getActivityInstanceId();
    }
    
    protected void performExecution(final ActivityExecution execution) throws Exception {
        this.leave(execution);
    }
    
    protected void postExecution(final ActivityExecution execution) throws Exception {
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        this.performExecution(execution);
    }
}
