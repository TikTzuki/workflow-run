// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.util.ClassLoaderUtil;
import javax.servlet.ServletContextEvent;
import org.zik.bpm.application.ProcessApplicationReference;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import org.zik.bpm.application.AbstractProcessApplication;

public class ServletProcessApplication extends AbstractProcessApplication implements ServletContextListener
{
    protected String servletContextName;
    protected String servletContextPath;
    protected ProcessApplicationReferenceImpl reference;
    protected ClassLoader processApplicationClassloader;
    protected ServletContext servletContext;
    
    @Override
    protected String autodetectProcessApplicationName() {
        String name = (this.servletContextName != null && !this.servletContextName.isEmpty()) ? this.servletContextName : this.servletContextPath;
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name;
    }
    
    public ProcessApplicationReference getReference() {
        if (this.reference == null) {
            this.reference = new ProcessApplicationReferenceImpl(this);
        }
        return this.reference;
    }
    
    public void contextInitialized(final ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        this.servletContextPath = this.servletContext.getContextPath();
        this.servletContextName = sce.getServletContext().getServletContextName();
        this.processApplicationClassloader = this.initProcessApplicationClassloader(sce);
        this.deploy();
    }
    
    protected ClassLoader initProcessApplicationClassloader(final ServletContextEvent sce) {
        if (this.isServlet30ApiPresent(sce) && this.getClass().equals(ServletProcessApplication.class)) {
            return ClassLoaderUtil.getServletContextClassloader(sce);
        }
        return ClassLoaderUtil.getClassloader(this.getClass());
    }
    
    private boolean isServlet30ApiPresent(final ServletContextEvent sce) {
        return sce.getServletContext().getMajorVersion() >= 3;
    }
    
    @Override
    public ClassLoader getProcessApplicationClassloader() {
        return this.processApplicationClassloader;
    }
    
    public void contextDestroyed(final ServletContextEvent sce) {
        this.undeploy();
        if (this.reference != null) {
            this.reference.clear();
        }
        this.reference = null;
    }
    
    @Override
    public Map<String, String> getProperties() {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("servletContextPath", this.servletContextPath);
        return properties;
    }
    
    public ServletContext getServletContext() {
        return this.servletContext;
    }
}
