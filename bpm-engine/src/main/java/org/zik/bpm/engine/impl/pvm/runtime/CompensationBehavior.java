// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime;

import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public class CompensationBehavior
{
    public static boolean executesNonScopeCompensationHandler(final PvmExecutionImpl execution) {
        final ActivityImpl activity = execution.getActivity();
        return execution.isScope() && activity != null && activity.isCompensationHandler() && !activity.isScope();
    }
    
    public static boolean isCompensationThrowing(final PvmExecutionImpl execution) {
        final ActivityImpl currentActivity = execution.getActivity();
        if (currentActivity != null) {
            final Boolean isCompensationThrowing = (Boolean)currentActivity.getProperty("throwsCompensation");
            if (isCompensationThrowing != null && isCompensationThrowing) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean executesDefaultCompensationHandler(final PvmExecutionImpl scopeExecution) {
        final ActivityImpl currentActivity = scopeExecution.getActivity();
        return currentActivity != null && scopeExecution.isScope() && currentActivity.isScope() && !scopeExecution.getNonEventScopeExecutions().isEmpty() && !isCompensationThrowing(scopeExecution);
    }
    
    public static String getParentActivityInstanceId(final PvmExecutionImpl execution) {
        final Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping = execution.createActivityExecutionMapping();
        final PvmExecutionImpl parentScopeExecution = activityExecutionMapping.get(execution.getActivity().getFlowScope());
        return parentScopeExecution.getParentActivityInstanceId();
    }
}
