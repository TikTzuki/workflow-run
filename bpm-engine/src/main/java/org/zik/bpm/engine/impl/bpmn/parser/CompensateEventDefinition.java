// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import java.io.Serializable;

public class CompensateEventDefinition implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String activityRef;
    protected boolean waitForCompletion;
    
    public String getActivityRef() {
        return this.activityRef;
    }
    
    public void setActivityRef(final String activityRef) {
        this.activityRef = activityRef;
    }
    
    public boolean isWaitForCompletion() {
        return this.waitForCompletion;
    }
    
    public void setWaitForCompletion(final boolean waitForCompletion) {
        this.waitForCompletion = waitForCompletion;
    }
}
