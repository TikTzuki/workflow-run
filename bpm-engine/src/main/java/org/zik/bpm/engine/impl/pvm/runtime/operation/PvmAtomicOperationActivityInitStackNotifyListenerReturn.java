// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.runtime.InstantiationStack;
import org.zik.bpm.engine.impl.pvm.runtime.ScopeInstantiationContext;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityInitStackNotifyListenerReturn extends PvmAtomicOperationActivityInstanceStart
{
    @Override
    public String getCanonicalName() {
        return "activity-init-stack-notify-listener-return";
    }
    
    @Override
    protected ScopeImpl getScope(final PvmExecutionImpl execution) {
        final ActivityImpl activity = execution.getActivity();
        if (activity != null) {
            return activity;
        }
        final PvmExecutionImpl parent = execution.getParent();
        if (parent != null) {
            return this.getScope(execution.getParent());
        }
        return execution.getProcessDefinition();
    }
    
    @Override
    protected String getEventName() {
        return "start";
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        super.eventNotificationsCompleted(execution);
        final ScopeInstantiationContext startContext = execution.getScopeInstantiationContext();
        final InstantiationStack instantiationStack = startContext.getInstantiationStack();
        if (instantiationStack.getActivities().isEmpty()) {
            execution.disposeScopeInstantiationContext();
            return;
        }
        execution.setActivity(null);
        execution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationActivityInitStackNotifyListenerReturn.ACTIVITY_INIT_STACK_AND_RETURN);
    }
}
