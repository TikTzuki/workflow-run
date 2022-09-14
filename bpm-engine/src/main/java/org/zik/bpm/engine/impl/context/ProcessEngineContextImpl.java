// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.context;

public class ProcessEngineContextImpl
{
    protected static ThreadLocal<Boolean> commandContextNew;
    
    public static boolean get() {
        return ProcessEngineContextImpl.commandContextNew.get();
    }
    
    public static void set(final boolean requiresNew) {
        ProcessEngineContextImpl.commandContextNew.set(requiresNew);
    }
    
    public static boolean consume() {
        final boolean isNewCommandContext = get();
        clear();
        return isNewCommandContext;
    }
    
    public static void clear() {
        ProcessEngineContextImpl.commandContextNew.set(false);
    }
    
    static {
        ProcessEngineContextImpl.commandContextNew = new ThreadLocal<Boolean>() {
            @Override
            protected Boolean initialValue() {
                return Boolean.FALSE;
            }
        };
    }
}
