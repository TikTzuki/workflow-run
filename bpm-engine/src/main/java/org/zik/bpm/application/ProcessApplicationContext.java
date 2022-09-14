// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

import java.util.concurrent.Callable;
import org.zik.bpm.application.impl.ProcessApplicationContextImpl;
import org.zik.bpm.application.impl.ProcessApplicationIdentifier;

public class ProcessApplicationContext
{
    public static void setCurrentProcessApplication(final String processApplicationName) {
        ProcessApplicationContextImpl.set(new ProcessApplicationIdentifier(processApplicationName));
    }
    
    public static void setCurrentProcessApplication(final ProcessApplicationReference reference) {
        ProcessApplicationContextImpl.set(new ProcessApplicationIdentifier(reference));
    }
    
    public static void setCurrentProcessApplication(final ProcessApplicationInterface processApplication) {
        ProcessApplicationContextImpl.set(new ProcessApplicationIdentifier(processApplication));
    }
    
    public static void clear() {
        ProcessApplicationContextImpl.clear();
    }
    
    public static <T> T withProcessApplicationContext(final Callable<T> callable, final String processApplicationName) throws Exception {
        try {
            setCurrentProcessApplication(processApplicationName);
            return callable.call();
        }
        finally {
            clear();
        }
    }
    
    public static <T> T withProcessApplicationContext(final Callable<T> callable, final ProcessApplicationReference reference) throws Exception {
        try {
            setCurrentProcessApplication(reference);
            return callable.call();
        }
        finally {
            clear();
        }
    }
    
    public static <T> T withProcessApplicationContext(final Callable<T> callable, final ProcessApplicationInterface processApplication) throws Exception {
        try {
            setCurrentProcessApplication(processApplication);
            return callable.call();
        }
        finally {
            clear();
        }
    }
}
