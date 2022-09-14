// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface ExecutionListener extends DelegateListener<DelegateExecution> {
    public static final String EVENTNAME_START = "start";
    public static final String EVENTNAME_END = "end";
    public static final String EVENTNAME_TAKE = "take";

    void notify(final DelegateExecution p0) throws Exception;
}
