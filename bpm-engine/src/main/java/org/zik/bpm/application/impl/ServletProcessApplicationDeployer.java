// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import javax.servlet.ServletException;
import org.zik.bpm.application.AbstractProcessApplication;
import java.util.Collection;
import java.util.HashSet;
import javax.servlet.ServletContext;
import java.util.Set;
import org.zik.bpm.application.ProcessApplication;
import javax.servlet.annotation.HandlesTypes;
import javax.servlet.ServletContainerInitializer;

@HandlesTypes({ ProcessApplication.class })
public class ServletProcessApplicationDeployer implements ServletContainerInitializer
{
    private static ProcessApplicationLogger LOG;
    
    public void onStartup(Set<Class<?>> c, final ServletContext ctx) throws ServletException {
        if (c == null || c.isEmpty()) {
            return;
        }
        if (c.contains(ProcessApplication.class)) {
            c = new HashSet<Class<?>>(c);
            c.remove(ProcessApplication.class);
        }
        final String contextPath = ctx.getContextPath();
        if (c.size() > 1) {
            throw ServletProcessApplicationDeployer.LOG.multiplePasException(c, contextPath);
        }
        if (c.size() == 1) {
            final Class<?> paClass = c.iterator().next();
            if (!AbstractProcessApplication.class.isAssignableFrom(paClass)) {
                throw ServletProcessApplicationDeployer.LOG.paWrongTypeException(paClass);
            }
            if (ServletProcessApplication.class.isAssignableFrom(paClass)) {
                ServletProcessApplicationDeployer.LOG.detectedPa(paClass);
                ctx.addListener(paClass.getName());
            }
        }
        else {
            ServletProcessApplicationDeployer.LOG.servletDeployerNoPaFound(contextPath);
        }
    }
    
    static {
        ServletProcessApplicationDeployer.LOG = ProcessEngineLogger.PROCESS_APPLICATION_LOGGER;
    }
}
