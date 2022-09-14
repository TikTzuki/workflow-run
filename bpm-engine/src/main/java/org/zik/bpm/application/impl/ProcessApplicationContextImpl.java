// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

public class ProcessApplicationContextImpl
{
    protected static ThreadLocal<ProcessApplicationIdentifier> currentProcessApplication;
    
    public static ProcessApplicationIdentifier get() {
        return ProcessApplicationContextImpl.currentProcessApplication.get();
    }
    
    public static void set(final ProcessApplicationIdentifier identifier) {
        ProcessApplicationContextImpl.currentProcessApplication.set(identifier);
    }
    
    public static void clear() {
        ProcessApplicationContextImpl.currentProcessApplication.remove();
    }
    
    static {
        ProcessApplicationContextImpl.currentProcessApplication = new ThreadLocal<ProcessApplicationIdentifier>();
    }
}
