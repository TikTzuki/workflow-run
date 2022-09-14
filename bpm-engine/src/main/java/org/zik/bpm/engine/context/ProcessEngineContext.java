// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.context;

import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.context.ProcessEngineContextImpl;

public class ProcessEngineContext
{
    public static void requiresNew() {
        ProcessEngineContextImpl.set(true);
    }
    
    public static void clear() {
        ProcessEngineContextImpl.clear();
    }
    
    public static <T> T withNewProcessEngineContext(final Callable<T> callable) throws Exception {
        try {
            requiresNew();
            return callable.call();
        }
        finally {
            clear();
        }
    }
}
