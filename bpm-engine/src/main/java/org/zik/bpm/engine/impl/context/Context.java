// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.context;

import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.application.InvocationContext;
import java.util.concurrent.Callable;
import java.util.ArrayDeque;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutorContext;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.interceptor.CommandInvocationContext;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Deque;

public class Context
{
    protected static ThreadLocal<Deque<CommandContext>> commandContextThreadLocal;
    protected static ThreadLocal<Deque<CommandInvocationContext>> commandInvocationContextThreadLocal;
    protected static ThreadLocal<Deque<ProcessEngineConfigurationImpl>> processEngineConfigurationStackThreadLocal;
    protected static ThreadLocal<Deque<CoreExecutionContext<? extends CoreExecution>>> executionContextStackThreadLocal;
    protected static ThreadLocal<JobExecutorContext> jobExecutorContextThreadLocal;
    protected static ThreadLocal<Deque<ProcessApplicationReference>> processApplicationContext;
    
    public static CommandContext getCommandContext() {
        final Deque<CommandContext> stack = getStack(Context.commandContextThreadLocal);
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }
    
    public static void setCommandContext(final CommandContext commandContext) {
        getStack(Context.commandContextThreadLocal).push(commandContext);
    }
    
    public static void removeCommandContext() {
        getStack(Context.commandContextThreadLocal).pop();
    }
    
    public static CommandInvocationContext getCommandInvocationContext() {
        final Deque<CommandInvocationContext> stack = getStack(Context.commandInvocationContextThreadLocal);
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }
    
    public static void setCommandInvocationContext(final CommandInvocationContext commandInvocationContext) {
        getStack(Context.commandInvocationContextThreadLocal).push(commandInvocationContext);
    }
    
    public static void removeCommandInvocationContext() {
        final Deque<CommandInvocationContext> stack = getStack(Context.commandInvocationContextThreadLocal);
        final CommandInvocationContext currentContext = stack.pop();
        if (stack.isEmpty()) {
            if (getJobExecutorContext() == null) {
                currentContext.getProcessDataContext().clearMdc();
            }
        }
        else {
            stack.peek().getProcessDataContext().updateMdcFromCurrentValues();
        }
    }
    
    public static ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        final Deque<ProcessEngineConfigurationImpl> stack = getStack(Context.processEngineConfigurationStackThreadLocal);
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }
    
    public static void setProcessEngineConfiguration(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        getStack(Context.processEngineConfigurationStackThreadLocal).push(processEngineConfiguration);
    }
    
    public static void removeProcessEngineConfiguration() {
        getStack(Context.processEngineConfigurationStackThreadLocal).pop();
    }
    
    @Deprecated
    public static ExecutionContext getExecutionContext() {
        return getBpmnExecutionContext();
    }
    
    public static BpmnExecutionContext getBpmnExecutionContext() {
        return (BpmnExecutionContext)getCoreExecutionContext();
    }
    
    public static CaseExecutionContext getCaseExecutionContext() {
        return (CaseExecutionContext)getCoreExecutionContext();
    }
    
    public static CoreExecutionContext<? extends CoreExecution> getCoreExecutionContext() {
        final Deque<CoreExecutionContext<? extends CoreExecution>> stack = getStack(Context.executionContextStackThreadLocal);
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }
    
    public static void setExecutionContext(final ExecutionEntity execution) {
        getStack((ThreadLocal<Deque<BpmnExecutionContext>>)Context.executionContextStackThreadLocal).push(new BpmnExecutionContext(execution));
    }
    
    public static void setExecutionContext(final CaseExecutionEntity execution) {
        getStack((ThreadLocal<Deque<CaseExecutionContext>>)Context.executionContextStackThreadLocal).push(new CaseExecutionContext(execution));
    }
    
    public static void removeExecutionContext() {
        getStack(Context.executionContextStackThreadLocal).pop();
    }
    
    protected static <T> Deque<T> getStack(final ThreadLocal<Deque<T>> threadLocal) {
        Deque<T> stack = threadLocal.get();
        if (stack == null) {
            stack = new ArrayDeque<T>();
            threadLocal.set(stack);
        }
        return stack;
    }
    
    public static JobExecutorContext getJobExecutorContext() {
        return Context.jobExecutorContextThreadLocal.get();
    }
    
    public static void setJobExecutorContext(final JobExecutorContext jobExecutorContext) {
        Context.jobExecutorContextThreadLocal.set(jobExecutorContext);
    }
    
    public static void removeJobExecutorContext() {
        Context.jobExecutorContextThreadLocal.remove();
    }
    
    public static ProcessApplicationReference getCurrentProcessApplication() {
        final Deque<ProcessApplicationReference> stack = getStack(Context.processApplicationContext);
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }
    
    public static void setCurrentProcessApplication(final ProcessApplicationReference reference) {
        final Deque<ProcessApplicationReference> stack = getStack(Context.processApplicationContext);
        stack.push(reference);
    }
    
    public static void removeCurrentProcessApplication() {
        final Deque<ProcessApplicationReference> stack = getStack(Context.processApplicationContext);
        stack.pop();
    }
    
    public static <T> T executeWithinProcessApplication(final Callable<T> callback, final ProcessApplicationReference processApplicationReference) {
        return executeWithinProcessApplication(callback, processApplicationReference, null);
    }
    
    public static <T> T executeWithinProcessApplication(final Callable<T> callback, final ProcessApplicationReference processApplicationReference, final InvocationContext invocationContext) {
        final String paName = processApplicationReference.getName();
        try {
            final ProcessApplicationInterface processApplication = processApplicationReference.getProcessApplication();
            setCurrentProcessApplication(processApplicationReference);
            try {
                final ProcessApplicationClassloaderInterceptor<T> wrappedCallback = new ProcessApplicationClassloaderInterceptor<T>(callback);
                return processApplication.execute(wrappedCallback, invocationContext);
            }
            catch (Exception e) {
                if (e.getCause() != null && e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException)e.getCause();
                }
                throw new ProcessEngineException("Unexpected exeption while executing within process application ", e);
            }
            finally {
                removeCurrentProcessApplication();
            }
        }
        catch (ProcessApplicationUnavailableException e2) {
            throw new ProcessEngineException("Cannot switch to process application '" + paName + "' for execution: " + e2.getMessage(), e2);
        }
    }
    
    static {
        Context.commandContextThreadLocal = new ThreadLocal<Deque<CommandContext>>();
        Context.commandInvocationContextThreadLocal = new ThreadLocal<Deque<CommandInvocationContext>>();
        Context.processEngineConfigurationStackThreadLocal = new ThreadLocal<Deque<ProcessEngineConfigurationImpl>>();
        Context.executionContextStackThreadLocal = new ThreadLocal<Deque<CoreExecutionContext<? extends CoreExecution>>>();
        Context.jobExecutorContextThreadLocal = new ThreadLocal<JobExecutorContext>();
        Context.processApplicationContext = new ThreadLocal<Deque<ProcessApplicationReference>>();
    }
}
