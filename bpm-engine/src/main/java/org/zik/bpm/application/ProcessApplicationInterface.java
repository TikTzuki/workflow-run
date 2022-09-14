// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

import org.zik.bpm.engine.delegate.TaskListener;
import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.engine.repository.DeploymentBuilder;
import org.zik.bpm.engine.impl.javax.el.BeanELResolver;
import org.zik.bpm.engine.impl.javax.el.ELResolver;
import java.util.Map;
import java.util.concurrent.Callable;

public interface ProcessApplicationInterface
{
    void deploy();
    
    void undeploy();
    
    String getName();
    
    ProcessApplicationReference getReference();
    
    ProcessApplicationInterface getRawObject();
    
     <T> T execute(final Callable<T> p0) throws ProcessApplicationExecutionException;
    
     <T> T execute(final Callable<T> p0, final InvocationContext p1) throws ProcessApplicationExecutionException;
    
    ClassLoader getProcessApplicationClassloader();
    
    Map<String, String> getProperties();
    
    ELResolver getElResolver();
    
    BeanELResolver getBeanElResolver();
    
    void createDeployment(final String p0, final DeploymentBuilder p1);
    
    ExecutionListener getExecutionListener();
    
    TaskListener getTaskListener();
}
