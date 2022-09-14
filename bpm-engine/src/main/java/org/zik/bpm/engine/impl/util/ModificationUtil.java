// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ModificationObserverBehavior;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public abstract class ModificationUtil
{
    public static void handleChildRemovalInScope(ExecutionEntity removedExecution) {
        ActivityImpl activity = removedExecution.getActivity();
        if (activity == null) {
            if (removedExecution.getSuperExecution() == null) {
                return;
            }
            removedExecution = removedExecution.getSuperExecution();
            activity = removedExecution.getActivity();
            if (activity == null) {
                return;
            }
        }
        final ScopeImpl flowScope = activity.getFlowScope();
        final PvmExecutionImpl scopeExecution = removedExecution.getParentScopeExecution(false);
        final PvmExecutionImpl executionInParentScope = removedExecution.isConcurrent() ? removedExecution : removedExecution.getParent();
        if (flowScope.getActivityBehavior() != null && flowScope.getActivityBehavior() instanceof ModificationObserverBehavior) {
            final ModificationObserverBehavior behavior = (ModificationObserverBehavior)flowScope.getActivityBehavior();
            behavior.destroyInnerInstance(executionInParentScope);
        }
        else if (executionInParentScope.isConcurrent()) {
            executionInParentScope.remove();
            scopeExecution.tryPruneLastConcurrentChild();
            scopeExecution.forceUpdate();
        }
    }
}
