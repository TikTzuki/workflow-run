// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.application.InvocationContext;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.runtime.AtomicOperation;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.List;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class CommandInvocationContext
{
    private static final CommandLogger LOG;
    protected Throwable throwable;
    protected Command<?> command;
    protected boolean isExecuting;
    protected List<AtomicOperationInvocation> queuedInvocations;
    protected BpmnStackTrace bpmnStackTrace;
    protected ProcessDataContext processDataContext;
    
    public CommandInvocationContext(final Command<?> command, final ProcessEngineConfigurationImpl configuration) {
        this.isExecuting = false;
        this.queuedInvocations = new ArrayList<AtomicOperationInvocation>();
        this.bpmnStackTrace = new BpmnStackTrace();
        this.command = command;
        this.processDataContext = new ProcessDataContext(configuration);
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    public Command<?> getCommand() {
        return this.command;
    }
    
    public void trySetThrowable(final Throwable t) {
        if (this.throwable == null) {
            this.throwable = t;
        }
        else {
            CommandInvocationContext.LOG.maskedExceptionInCommandContext(this.throwable);
        }
    }
    
    public void performOperation(final AtomicOperation executionOperation, final ExecutionEntity execution) {
        this.performOperation(executionOperation, execution, false);
    }
    
    public void performOperationAsync(final AtomicOperation executionOperation, final ExecutionEntity execution) {
        this.performOperation(executionOperation, execution, true);
    }
    
    public void performOperation(final AtomicOperation executionOperation, final ExecutionEntity execution, final boolean performAsync) {
        final AtomicOperationInvocation invocation = new AtomicOperationInvocation(executionOperation, execution, performAsync);
        this.queuedInvocations.add(0, invocation);
        this.performNext();
    }
    
    protected void performNext() {
        final AtomicOperationInvocation nextInvocation = this.queuedInvocations.get(0);
        if (nextInvocation.operation.isAsyncCapable() && this.isExecuting) {
            return;
        }
        final ProcessApplicationReference targetProcessApplication = this.getTargetProcessApplication(nextInvocation.execution);
        if (this.requiresContextSwitch(targetProcessApplication)) {
            Context.executeWithinProcessApplication((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    CommandInvocationContext.this.performNext();
                    return null;
                }
            }, targetProcessApplication, new InvocationContext(nextInvocation.execution));
        }
        else if (!nextInvocation.operation.isAsyncCapable()) {
            this.invokeNext();
        }
        else {
            try {
                this.isExecuting = true;
                while (!this.queuedInvocations.isEmpty()) {
                    this.invokeNext();
                }
            }
            finally {
                this.isExecuting = false;
            }
        }
    }
    
    protected void invokeNext() {
        final AtomicOperationInvocation invocation = this.queuedInvocations.remove(0);
        try {
            invocation.execute(this.bpmnStackTrace, this.processDataContext);
        }
        catch (RuntimeException e) {
            this.bpmnStackTrace.printStackTrace(Context.getProcessEngineConfiguration().isBpmnStacktraceVerbose());
            throw e;
        }
    }
    
    protected boolean requiresContextSwitch(final ProcessApplicationReference processApplicationReference) {
        return ProcessApplicationContextUtil.requiresContextSwitch(processApplicationReference);
    }
    
    protected ProcessApplicationReference getTargetProcessApplication(final ExecutionEntity execution) {
        return ProcessApplicationContextUtil.getTargetProcessApplication(execution);
    }
    
    public void rethrow() {
        if (this.throwable == null) {
            return;
        }
        if (this.throwable instanceof Error) {
            throw (Error)this.throwable;
        }
        if (this.throwable instanceof RuntimeException) {
            throw (RuntimeException)this.throwable;
        }
        throw new ProcessEngineException("exception while executing command " + this.command, this.throwable);
    }
    
    public ProcessDataContext getProcessDataContext() {
        return this.processDataContext;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
