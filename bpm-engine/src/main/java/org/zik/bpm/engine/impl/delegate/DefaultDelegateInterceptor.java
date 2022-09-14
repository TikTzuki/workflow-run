// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.context.CoreExecutionContext;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.application.InvocationContext;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.interceptor.DelegateInterceptor;

public class DefaultDelegateInterceptor implements DelegateInterceptor
{
    @Override
    public void handleInvocation(final DelegateInvocation invocation) throws Exception {
        final ProcessApplicationReference processApplication = this.getProcessApplicationForInvocation(invocation);
        if (processApplication != null && ProcessApplicationContextUtil.requiresContextSwitch(processApplication)) {
            Context.executeWithinProcessApplication((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    DefaultDelegateInterceptor.this.handleInvocation(invocation);
                    return null;
                }
            }, processApplication, new InvocationContext(invocation.getContextExecution()));
        }
        else {
            this.handleInvocationInContext(invocation);
        }
    }
    
    protected void handleInvocationInContext(final DelegateInvocation invocation) throws Exception {
        final CommandContext commandContext = Context.getCommandContext();
        final boolean wasAuthorizationCheckEnabled = commandContext.isAuthorizationCheckEnabled();
        final boolean wasUserOperationLogEnabled = commandContext.isUserOperationLogEnabled();
        final BaseDelegateExecution contextExecution = invocation.getContextExecution();
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        boolean popExecutionContext = false;
        try {
            if (!configuration.isAuthorizationEnabledForCustomCode()) {
                commandContext.disableAuthorizationCheck();
            }
            try {
                commandContext.disableUserOperationLog();
                try {
                    if (contextExecution != null && !this.isCurrentContextExecution(contextExecution)) {
                        popExecutionContext = this.setExecutionContext(contextExecution);
                    }
                    invocation.proceed();
                }
                finally {
                    if (popExecutionContext) {
                        Context.removeExecutionContext();
                    }
                }
            }
            finally {
                if (wasUserOperationLogEnabled) {
                    commandContext.enableUserOperationLog();
                }
            }
        }
        finally {
            if (wasAuthorizationCheckEnabled) {
                commandContext.enableAuthorizationCheck();
            }
        }
    }
    
    protected boolean setExecutionContext(final BaseDelegateExecution execution) {
        if (execution instanceof ExecutionEntity) {
            Context.setExecutionContext((ExecutionEntity)execution);
            return true;
        }
        if (execution instanceof CaseExecutionEntity) {
            Context.setExecutionContext((CaseExecutionEntity)execution);
            return true;
        }
        return false;
    }
    
    protected boolean isCurrentContextExecution(final BaseDelegateExecution execution) {
        final CoreExecutionContext<?> coreExecutionContext = Context.getCoreExecutionContext();
        return coreExecutionContext != null && coreExecutionContext.getExecution() == execution;
    }
    
    protected ProcessApplicationReference getProcessApplicationForInvocation(final DelegateInvocation invocation) {
        final BaseDelegateExecution contextExecution = invocation.getContextExecution();
        final ResourceDefinitionEntity contextResource = invocation.getContextResource();
        if (contextExecution != null) {
            return ProcessApplicationContextUtil.getTargetProcessApplication((CoreExecution)contextExecution);
        }
        if (contextResource != null) {
            return ProcessApplicationContextUtil.getTargetProcessApplication(contextResource);
        }
        return null;
    }
}
