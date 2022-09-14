// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface TaskListener
{
    public static final String EVENTNAME_CREATE = "create";
    public static final String EVENTNAME_ASSIGNMENT = "assignment";
    public static final String EVENTNAME_COMPLETE = "complete";
    public static final String EVENTNAME_UPDATE = "update";
    public static final String EVENTNAME_DELETE = "delete";
    public static final String EVENTNAME_TIMEOUT = "timeout";
    
    void notify(final DelegateTask p0);
}
