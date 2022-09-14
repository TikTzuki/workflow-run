// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.model;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.core.delegate.CoreActivityBehavior;
import java.util.List;
import java.util.Iterator;
import org.zik.bpm.engine.impl.core.variable.mapping.IoMapping;

public abstract class CoreActivity extends CoreModelElement
{
    private static final long serialVersionUID = 1L;
    protected IoMapping ioMapping;
    
    public CoreActivity(final String id) {
        super(id);
    }
    
    public CoreActivity findActivity(final String activityId) {
        final CoreActivity localActivity = this.getChildActivity(activityId);
        if (localActivity != null) {
            return localActivity;
        }
        for (final CoreActivity activity : this.getActivities()) {
            final CoreActivity nestedActivity = activity.findActivity(activityId);
            if (nestedActivity != null) {
                return nestedActivity;
            }
        }
        return null;
    }
    
    public CoreActivity createActivity() {
        return this.createActivity(null);
    }
    
    public abstract CoreActivity getChildActivity(final String p0);
    
    public abstract CoreActivity createActivity(final String p0);
    
    public abstract List<? extends CoreActivity> getActivities();
    
    public abstract CoreActivityBehavior<? extends BaseDelegateExecution> getActivityBehavior();
    
    public IoMapping getIoMapping() {
        return this.ioMapping;
    }
    
    public void setIoMapping(final IoMapping ioMapping) {
        this.ioMapping = ioMapping;
    }
    
    @Override
    public String toString() {
        return "Activity(" + this.id + ")";
    }
}
