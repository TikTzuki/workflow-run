// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import java.util.List;

public class InstantiationStack
{
    protected List<PvmActivity> activities;
    protected PvmActivity targetActivity;
    protected PvmTransition targetTransition;
    
    public InstantiationStack(final List<PvmActivity> activities) {
        this.activities = activities;
    }
    
    public InstantiationStack(final List<PvmActivity> activities, final PvmActivity targetActivity, final PvmTransition targetTransition) {
        EnsureUtil.ensureOnlyOneNotNull("target must be either a transition or an activity", targetActivity, targetTransition);
        this.activities = activities;
        this.targetActivity = targetActivity;
        this.targetTransition = targetTransition;
    }
    
    public List<PvmActivity> getActivities() {
        return this.activities;
    }
    
    public PvmTransition getTargetTransition() {
        return this.targetTransition;
    }
    
    public PvmActivity getTargetActivity() {
        return this.targetActivity;
    }
}
