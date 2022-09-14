// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;

public class ActivityStackCollector implements TreeVisitor<ScopeImpl>
{
    protected List<PvmActivity> activityStack;
    
    public ActivityStackCollector() {
        this.activityStack = new ArrayList<PvmActivity>();
    }
    
    @Override
    public void visit(final ScopeImpl scope) {
        if (scope != null && PvmActivity.class.isAssignableFrom(scope.getClass())) {
            this.activityStack.add((PvmActivity)scope);
        }
    }
    
    public List<PvmActivity> getActivityStack() {
        return this.activityStack;
    }
}
