// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface CaseExecutionListener extends DelegateListener<DelegateCaseExecution>
{
    public static final String CREATE = "create";
    public static final String ENABLE = "enable";
    public static final String DISABLE = "disable";
    public static final String RE_ENABLE = "reenable";
    public static final String START = "start";
    public static final String MANUAL_START = "manualStart";
    public static final String COMPLETE = "complete";
    public static final String RE_ACTIVATE = "reactivate";
    public static final String TERMINATE = "terminate";
    public static final String EXIT = "exit";
    public static final String PARENT_TERMINATE = "parentTerminate";
    public static final String SUSPEND = "suspend";
    public static final String RESUME = "resume";
    public static final String PARENT_SUSPEND = "parentSuspend";
    public static final String PARENT_RESUME = "parentResume";
    public static final String CLOSE = "close";
    public static final String OCCUR = "occur";
    
    void notify(final DelegateCaseExecution p0) throws Exception;
}
