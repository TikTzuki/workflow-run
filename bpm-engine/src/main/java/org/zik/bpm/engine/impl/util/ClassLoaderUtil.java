// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.ProcessEngine;
import javax.servlet.ServletContextEvent;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ClassLoaderUtil
{
    public static ClassLoader getContextClassloader() {
        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return Thread.currentThread().getContextClassLoader();
                }
            });
        }
        return Thread.currentThread().getContextClassLoader();
    }
    
    public static ClassLoader getClassloader(final Class<?> clazz) {
        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return clazz.getClassLoader();
                }
            });
        }
        return clazz.getClassLoader();
    }
    
    public static void setContextClassloader(final ClassLoader classLoader) {
        if (System.getSecurityManager() != null) {
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Void>() {
                @Override
                public Void run() {
                    Thread.currentThread().setContextClassLoader(classLoader);
                    return null;
                }
            });
        }
        else {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }
    
    public static ClassLoader getServletContextClassloader(final ServletContextEvent sce) {
        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return sce.getServletContext().getClassLoader();
                }
            });
        }
        return sce.getServletContext().getClassLoader();
    }
    
    public static ClassLoader switchToProcessEngineClassloader() {
        final ClassLoader currentClassloader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(ProcessEngine.class.getClassLoader());
        return currentClassloader;
    }
}
