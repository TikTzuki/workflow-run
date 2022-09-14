// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.helper.ErrorPropagationException;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnExceptionHandler;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.core.operation.AbstractEventAtomicOperation;

public abstract class AbstractPvmEventAtomicOperation extends AbstractEventAtomicOperation<PvmExecutionImpl> implements PvmAtomicOperation
{
    @Override
    protected abstract CoreModelElement getScope(final PvmExecutionImpl p0);
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
    
    @Override
    protected void eventNotificationsFailed(final PvmExecutionImpl execution, final Exception exception) {
        if (this.shouldHandleFailureAsBpmnError()) {
            final ActivityExecution activityExecution = execution;
            try {
                this.resetListeners(execution);
                BpmnExceptionHandler.propagateException(activityExecution, exception);
            }
            catch (ErrorPropagationException e2) {
                super.eventNotificationsFailed(execution, exception);
            }
            catch (Exception e) {
                super.eventNotificationsFailed(execution, e);
            }
        }
        else {
            super.eventNotificationsFailed(execution, exception);
        }
    }
    
    public boolean shouldHandleFailureAsBpmnError() {
        return false;
    }
}
