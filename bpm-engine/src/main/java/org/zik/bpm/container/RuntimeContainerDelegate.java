// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container;

import org.zik.bpm.container.impl.RuntimeContainerDelegateImpl;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.ProcessApplicationService;
import org.zik.bpm.ProcessEngineService;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.engine.ProcessEngine;

public interface RuntimeContainerDelegate
{
    public static final RuntimeContainerDelegateInstance INSTANCE = new RuntimeContainerDelegateInstance();
    
    void registerProcessEngine(final ProcessEngine p0);
    
    void unregisterProcessEngine(final ProcessEngine p0);
    
    void deployProcessApplication(final AbstractProcessApplication p0);
    
    void undeployProcessApplication(final AbstractProcessApplication p0);
    
    ProcessEngineService getProcessEngineService();
    
    ProcessApplicationService getProcessApplicationService();
    
    ExecutorService getExecutorService();
    
    ProcessApplicationReference getDeployedProcessApplication(final String p0);
    
    public static class RuntimeContainerDelegateInstance
    {
        private RuntimeContainerDelegate delegate;
        
        private RuntimeContainerDelegateInstance() {
            this.delegate = new RuntimeContainerDelegateImpl();
        }
        
        public RuntimeContainerDelegate get() {
            return this.delegate;
        }
        
        public void set(final RuntimeContainerDelegate delegate) {
            this.delegate = delegate;
        }
    }
}
