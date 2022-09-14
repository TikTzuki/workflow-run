// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.operation;

import org.zik.bpm.engine.impl.pvm.PvmException;
import java.util.List;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateListener;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;

public abstract class AbstractEventAtomicOperation<T extends CoreExecution> implements CoreAtomicOperation<T>
{
    @Override
    public boolean isAsync(final T execution) {
        return false;
    }
    
    @Override
    public void execute(T execution) {
        final CoreModelElement scope = this.getScope(execution);
        final List<DelegateListener<? extends BaseDelegateExecution>> listeners = this.getListeners(scope, execution);
        final int listenerIndex = execution.getListenerIndex();
        if (listenerIndex == 0) {
            execution = this.eventNotificationsStarted(execution);
        }
        if (!this.isSkipNotifyListeners(execution)) {
            if (listeners.size() > listenerIndex) {
                execution.setEventName(this.getEventName());
                execution.setEventSource(scope);
                final DelegateListener<? extends BaseDelegateExecution> listener = listeners.get(listenerIndex);
                execution.setListenerIndex(listenerIndex + 1);
                try {
                    execution.invokeListener(listener);
                }
                catch (Exception ex) {
                    this.eventNotificationsFailed(execution, ex);
                    return;
                }
                execution.performOperationSync((CoreAtomicOperation<CoreExecution>)this);
            }
            else {
                this.resetListeners(execution);
                this.eventNotificationsCompleted(execution);
            }
        }
        else {
            this.eventNotificationsCompleted(execution);
        }
    }
    
    protected void resetListeners(final T execution) {
        execution.setListenerIndex(0);
        execution.setEventName(null);
        execution.setEventSource(null);
    }
    
    protected List<DelegateListener<? extends BaseDelegateExecution>> getListeners(final CoreModelElement scope, final T execution) {
        if (execution.isSkipCustomListeners()) {
            return scope.getBuiltInListeners(this.getEventName());
        }
        return scope.getListeners(this.getEventName());
    }
    
    protected boolean isSkipNotifyListeners(final T execution) {
        return false;
    }
    
    protected T eventNotificationsStarted(final T execution) {
        return execution;
    }
    
    protected abstract CoreModelElement getScope(final T p0);
    
    protected abstract String getEventName();
    
    protected abstract void eventNotificationsCompleted(final T p0);
    
    protected void eventNotificationsFailed(final T execution, final Exception exception) {
        if (exception instanceof RuntimeException) {
            throw (RuntimeException)exception;
        }
        throw new PvmException("couldn't execute event listener : " + exception.getMessage(), exception);
    }
}
