// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import java.util.List;
import javax.script.ScriptEngine;
import org.zik.bpm.engine.delegate.TaskListener;
import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.application.impl.DefaultElResolverLookup;
import java.util.Collections;
import java.util.Map;
import org.zik.bpm.engine.impl.util.ClassLoaderUtil;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.repository.DeploymentBuilder;
import org.zik.bpm.container.RuntimeContainerDelegate;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.zik.bpm.application.impl.ProcessApplicationScriptEnvironment;
import org.zik.bpm.engine.impl.javax.el.BeanELResolver;
import org.zik.bpm.engine.impl.javax.el.ELResolver;
import org.zik.bpm.application.impl.ProcessApplicationLogger;

public abstract class AbstractProcessApplication implements ProcessApplicationInterface
{
    private static ProcessApplicationLogger LOG;
    protected ELResolver processApplicationElResolver;
    protected BeanELResolver processApplicationBeanElResolver;
    protected ProcessApplicationScriptEnvironment processApplicationScriptEnvironment;
    protected VariableSerializers variableSerializers;
    protected boolean isDeployed;
    protected String defaultDeployToEngineName;
    
    public AbstractProcessApplication() {
        this.isDeployed = false;
        this.defaultDeployToEngineName = "default";
    }
    
    @Override
    public void deploy() {
        if (this.isDeployed) {
            AbstractProcessApplication.LOG.alreadyDeployed();
        }
        else {
            try {
                final ProcessApplicationReference reference = this.getReference();
                Context.setCurrentProcessApplication(reference);
                RuntimeContainerDelegate.INSTANCE.get().deployProcessApplication(this);
                this.isDeployed = true;
            }
            finally {
                Context.removeCurrentProcessApplication();
            }
        }
    }
    
    @Override
    public void undeploy() {
        if (!this.isDeployed) {
            AbstractProcessApplication.LOG.notDeployed();
        }
        else {
            RuntimeContainerDelegate.INSTANCE.get().undeployProcessApplication(this);
            this.isDeployed = false;
        }
    }
    
    @Override
    public void createDeployment(final String processArchiveName, final DeploymentBuilder deploymentBuilder) {
    }
    
    @Override
    public String getName() {
        final Class<? extends AbstractProcessApplication> processApplicationClass = this.getClass();
        String name = null;
        final ProcessApplication annotation = processApplicationClass.getAnnotation(ProcessApplication.class);
        if (annotation != null) {
            name = annotation.value();
            if (name == null || name.length() == 0) {
                name = annotation.name();
            }
        }
        if (name == null || name.length() == 0) {
            name = this.autodetectProcessApplicationName();
        }
        return name;
    }
    
    protected abstract String autodetectProcessApplicationName();
    
    @Override
    public <T> T execute(final Callable<T> callable) throws ProcessApplicationExecutionException {
        final ClassLoader originalClassloader = ClassLoaderUtil.getContextClassloader();
        final ClassLoader processApplicationClassloader = this.getProcessApplicationClassloader();
        try {
            ClassLoaderUtil.setContextClassloader(processApplicationClassloader);
            return callable.call();
        }
        catch (Exception e) {
            throw AbstractProcessApplication.LOG.processApplicationExecutionException(e);
        }
        finally {
            ClassLoaderUtil.setContextClassloader(originalClassloader);
        }
    }
    
    @Override
    public <T> T execute(final Callable<T> callable, final InvocationContext invocationContext) throws ProcessApplicationExecutionException {
        return this.execute(callable);
    }
    
    @Override
    public ClassLoader getProcessApplicationClassloader() {
        return ClassLoaderUtil.getClassloader(this.getClass());
    }
    
    @Override
    public ProcessApplicationInterface getRawObject() {
        return this;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return Collections.emptyMap();
    }
    
    @Override
    public ELResolver getElResolver() {
        if (this.processApplicationElResolver == null) {
            synchronized (this) {
                if (this.processApplicationElResolver == null) {
                    this.processApplicationElResolver = this.initProcessApplicationElResolver();
                }
            }
        }
        return this.processApplicationElResolver;
    }
    
    @Override
    public BeanELResolver getBeanElResolver() {
        if (this.processApplicationBeanElResolver == null) {
            synchronized (this) {
                if (this.processApplicationBeanElResolver == null) {
                    this.processApplicationBeanElResolver = new BeanELResolver();
                }
            }
        }
        return this.processApplicationBeanElResolver;
    }
    
    protected ELResolver initProcessApplicationElResolver() {
        return DefaultElResolverLookup.lookupResolver(this);
    }
    
    @Override
    public ExecutionListener getExecutionListener() {
        return null;
    }
    
    @Override
    public TaskListener getTaskListener() {
        return null;
    }
    
    public ScriptEngine getScriptEngineForName(final String name, final boolean cache) {
        return this.getProcessApplicationScriptEnvironment().getScriptEngineForName(name, cache);
    }
    
    public Map<String, List<ExecutableScript>> getEnvironmentScripts() {
        return this.getProcessApplicationScriptEnvironment().getEnvironmentScripts();
    }
    
    protected ProcessApplicationScriptEnvironment getProcessApplicationScriptEnvironment() {
        if (this.processApplicationScriptEnvironment == null) {
            synchronized (this) {
                if (this.processApplicationScriptEnvironment == null) {
                    this.processApplicationScriptEnvironment = new ProcessApplicationScriptEnvironment(this);
                }
            }
        }
        return this.processApplicationScriptEnvironment;
    }
    
    public VariableSerializers getVariableSerializers() {
        return this.variableSerializers;
    }
    
    public void setVariableSerializers(final VariableSerializers variableSerializers) {
        this.variableSerializers = variableSerializers;
    }
    
    public String getDefaultDeployToEngineName() {
        return this.defaultDeployToEngineName;
    }
    
    protected void setDefaultDeployToEngineName(final String defaultDeployToEngineName) {
        this.defaultDeployToEngineName = defaultDeployToEngineName;
    }
    
    static {
        AbstractProcessApplication.LOG = ProcessEngineLogger.PROCESS_APPLICATION_LOGGER;
    }
}
