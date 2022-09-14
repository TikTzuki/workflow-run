// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl.event;

import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.application.InvocationContext;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.delegate.DelegateTask;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.application.impl.ProcessApplicationLogger;
import org.zik.bpm.engine.delegate.TaskListener;
import org.zik.bpm.engine.delegate.ExecutionListener;

public class ProcessApplicationEventListenerDelegate implements ExecutionListener, TaskListener
{
    private static ProcessApplicationLogger LOG;
    
    @Override
    public void notify(final DelegateExecution execution) throws Exception {
        final Callable<Void> notification = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ProcessApplicationEventListenerDelegate.this.notifyExecutionListener(execution);
                return null;
            }
        };
        this.performNotification(execution, notification);
    }
    
    @Override
    public void notify(final DelegateTask delegateTask) {
        if (delegateTask.getExecution() == null) {
            ProcessApplicationEventListenerDelegate.LOG.taskNotRelatedToExecution(delegateTask);
        }
        else {
            final DelegateExecution execution = delegateTask.getExecution();
            final Callable<Void> notification = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    ProcessApplicationEventListenerDelegate.this.notifyTaskListener(delegateTask);
                    return null;
                }
            };
            try {
                this.performNotification(execution, notification);
            }
            catch (Exception e) {
                throw ProcessApplicationEventListenerDelegate.LOG.exceptionWhileNotifyingPaTaskListener(e);
            }
        }
    }
    
    protected void performNotification(final DelegateExecution execution, final Callable<Void> notification) throws Exception {
        final ProcessApplicationReference processApp = ProcessApplicationContextUtil.getTargetProcessApplication((ExecutionEntity)execution);
        if (processApp == null) {
            ProcessApplicationEventListenerDelegate.LOG.noTargetProcessApplicationForExecution(execution);
        }
        else if (ProcessApplicationContextUtil.requiresContextSwitch(processApp)) {
            Context.executeWithinProcessApplication(notification, processApp, new InvocationContext(execution));
        }
        else {
            notification.call();
        }
    }
    
    protected void notifyExecutionListener(final DelegateExecution execution) throws Exception {
        final ProcessApplicationReference processApp = Context.getCurrentProcessApplication();
        try {
            final ProcessApplicationInterface processApplication = processApp.getProcessApplication();
            final ExecutionListener executionListener = processApplication.getExecutionListener();
            if (executionListener != null) {
                executionListener.notify(execution);
            }
            else {
                ProcessApplicationEventListenerDelegate.LOG.paDoesNotProvideExecutionListener(processApp.getName());
            }
        }
        catch (ProcessApplicationUnavailableException e) {
            ProcessApplicationEventListenerDelegate.LOG.cannotInvokeListenerPaUnavailable(processApp.getName(), e);
        }
    }
    
    protected void notifyTaskListener(final DelegateTask task) throws Exception {
        final ProcessApplicationReference processApp = Context.getCurrentProcessApplication();
        try {
            final ProcessApplicationInterface processApplication = processApp.getProcessApplication();
            final TaskListener taskListener = processApplication.getTaskListener();
            if (taskListener != null) {
                taskListener.notify(task);
            }
            else {
                ProcessApplicationEventListenerDelegate.LOG.paDoesNotProvideTaskListener(processApp.getName());
            }
        }
        catch (ProcessApplicationUnavailableException e) {
            ProcessApplicationEventListenerDelegate.LOG.cannotInvokeListenerPaUnavailable(processApp.getName(), e);
        }
    }
    
    static {
        ProcessApplicationEventListenerDelegate.LOG = ProcessApplicationLogger.PROCESS_APPLICATION_LOGGER;
    }
}
