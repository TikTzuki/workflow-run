// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.core.operation.AbstractEventAtomicOperation;

public abstract class AbstractCmmnEventAtomicOperation extends AbstractEventAtomicOperation<CmmnExecution> implements CmmnAtomicOperation
{
    @Override
    protected CmmnActivity getScope(final CmmnExecution execution) {
        return execution.getActivity();
    }
    
    @Override
    public boolean isAsync(final CmmnExecution execution) {
        return false;
    }
    
    @Override
    protected void eventNotificationsCompleted(final CmmnExecution execution) {
        this.repetition(execution);
        this.preTransitionNotification(execution);
        this.performTransitionNotification(execution);
        this.postTransitionNotification(execution);
    }
    
    protected void repetition(final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        behavior.repeat(execution, this.getEventName());
    }
    
    protected void preTransitionNotification(final CmmnExecution execution) {
    }
    
    protected void performTransitionNotification(final CmmnExecution execution) {
        final String eventName = this.getEventName();
        final CmmnExecution parent = execution.getParent();
        if (parent != null) {
            parent.handleChildTransition(execution, eventName);
        }
    }
    
    protected void postTransitionNotification(final CmmnExecution execution) {
    }
}
