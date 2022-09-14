// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.context;

import org.zik.bpm.engine.impl.util.ClassLoaderUtil;
import java.util.concurrent.Callable;

public class ProcessApplicationClassloaderInterceptor<T> implements Callable<T>
{
    private static ThreadLocal<ClassLoader> PA_CLASSLOADER;
    protected Callable<T> delegate;
    
    public ProcessApplicationClassloaderInterceptor(final Callable<T> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public T call() throws Exception {
        try {
            ProcessApplicationClassloaderInterceptor.PA_CLASSLOADER.set(ClassLoaderUtil.getContextClassloader());
            return this.delegate.call();
        }
        finally {
            ProcessApplicationClassloaderInterceptor.PA_CLASSLOADER.remove();
        }
    }
    
    public static ClassLoader getProcessApplicationClassLoader() {
        return ProcessApplicationClassloaderInterceptor.PA_CLASSLOADER.get();
    }
    
    static {
        ProcessApplicationClassloaderInterceptor.PA_CLASSLOADER = new ThreadLocal<ClassLoader>();
    }
}
