// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.container.RuntimeContainerDelegate;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.application.impl.ProcessApplicationIdentifier;
import org.zik.bpm.engine.impl.context.Context;
import java.util.concurrent.Callable;
import org.zik.bpm.application.impl.ProcessApplicationContextImpl;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class ProcessApplicationContextInterceptor extends CommandInterceptor
{
    private static final CommandLogger LOG;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    
    public ProcessApplicationContextInterceptor(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
    
    @Override
    public <T> T execute(final Command<T> command) {
        final ProcessApplicationIdentifier processApplicationIdentifier = ProcessApplicationContextImpl.get();
        if (processApplicationIdentifier != null) {
            ProcessApplicationContextImpl.clear();
            try {
                final ProcessApplicationReference reference = this.getPaReference(processApplicationIdentifier);
                return Context.executeWithinProcessApplication((Callable<T>)new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        return ProcessApplicationContextInterceptor.this.next.execute(command);
                    }
                }, reference);
            }
            finally {
                ProcessApplicationContextImpl.set(processApplicationIdentifier);
            }
        }
        return this.next.execute(command);
    }
    
    protected ProcessApplicationReference getPaReference(final ProcessApplicationIdentifier processApplicationIdentifier) {
        if (processApplicationIdentifier.getReference() != null) {
            return processApplicationIdentifier.getReference();
        }
        if (processApplicationIdentifier.getProcessApplication() != null) {
            return processApplicationIdentifier.getProcessApplication().getReference();
        }
        if (processApplicationIdentifier.getName() == null) {
            throw ProcessApplicationContextInterceptor.LOG.cannotReolvePa(processApplicationIdentifier);
        }
        final RuntimeContainerDelegate runtimeContainerDelegate = RuntimeContainerDelegate.INSTANCE.get();
        final ProcessApplicationReference reference = runtimeContainerDelegate.getDeployedProcessApplication(processApplicationIdentifier.getName());
        if (reference == null) {
            throw ProcessApplicationContextInterceptor.LOG.paWithNameNotRegistered(processApplicationIdentifier.getName());
        }
        return reference;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
