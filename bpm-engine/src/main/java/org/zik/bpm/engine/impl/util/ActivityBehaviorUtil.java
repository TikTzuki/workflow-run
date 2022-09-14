// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.PvmException;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public class ActivityBehaviorUtil
{
    public static CmmnActivityBehavior getActivityBehavior(final CmmnExecution execution) {
        final String id = execution.getId();
        final CmmnActivity activity = execution.getActivity();
        EnsureUtil.ensureNotNull(PvmException.class, "Case execution '" + id + "' has no current activity.", "activity", activity);
        final CmmnActivityBehavior behavior = activity.getActivityBehavior();
        EnsureUtil.ensureNotNull(PvmException.class, "There is no behavior specified in " + activity + " for case execution '" + id + "'.", "behavior", behavior);
        return behavior;
    }
    
    public static ActivityBehavior getActivityBehavior(final PvmExecutionImpl execution) {
        final String id = execution.getId();
        final PvmActivity activity = execution.getActivity();
        EnsureUtil.ensureNotNull(PvmException.class, "Execution '" + id + "' has no current activity.", "activity", activity);
        final ActivityBehavior behavior = activity.getActivityBehavior();
        EnsureUtil.ensureNotNull(PvmException.class, "There is no behavior specified in " + activity + " for execution '" + id + "'.", "behavior", behavior);
        return behavior;
    }
}
