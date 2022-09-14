// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.delegate;

import org.zik.bpm.engine.delegate.DelegateTask;

public interface TaskListener
{
    public static final String EVENTNAME_CREATE = "create";
    public static final String EVENTNAME_ASSIGNMENT = "assignment";
    public static final String EVENTNAME_COMPLETE = "complete";
    
    void notify(final DelegateTask p0);
}
