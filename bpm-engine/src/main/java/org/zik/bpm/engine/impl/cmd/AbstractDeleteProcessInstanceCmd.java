// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.runtime.ProcessInstance;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionManager;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.ProcessInstanceModificationBuilderImpl;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public abstract class AbstractDeleteProcessInstanceCmd
{
    protected boolean externallyTerminated;
    protected String deleteReason;
    protected boolean skipCustomListeners;
    protected boolean skipSubprocesses;
    protected boolean failIfNotExists;
    
    public AbstractDeleteProcessInstanceCmd() {
        this.failIfNotExists = true;
    }
    
    protected void checkDeleteProcessInstance(final ExecutionEntity execution, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteProcessInstance(execution);
        }
    }
    
    protected void deleteProcessInstance(final CommandContext commandContext, final String processInstanceId, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated, final boolean skipIoMappings, final boolean skipSubprocesses) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "processInstanceId is null", "processInstanceId", processInstanceId);
        final ExecutionManager executionManager = commandContext.getExecutionManager();
        final ExecutionEntity execution = executionManager.findExecutionById(processInstanceId);
        if (!this.failIfNotExists && execution == null) {
            return;
        }
        EnsureUtil.ensureNotNull(NotFoundException.class, "No process instance found for id '" + processInstanceId + "'", "processInstance", execution);
        this.checkDeleteProcessInstance(execution, commandContext);
        commandContext.getExecutionManager().deleteProcessInstance(processInstanceId, deleteReason, false, skipCustomListeners, externallyTerminated, skipIoMappings, skipSubprocesses);
        if (skipSubprocesses) {
            final List<ProcessInstance> superProcesslist = ((Query<T, ProcessInstance>)commandContext.getProcessEngineConfiguration().getRuntimeService().createProcessInstanceQuery().superProcessInstanceId(processInstanceId)).list();
            this.triggerHistoryEvent(superProcesslist);
        }
        final ExecutionEntity superExecution = execution.getSuperExecution();
        if (superExecution != null) {
            commandContext.runWithoutAuthorization((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() {
                    final ProcessInstanceModificationBuilderImpl builder = (ProcessInstanceModificationBuilderImpl)new ProcessInstanceModificationBuilderImpl(commandContext, superExecution.getProcessInstanceId(), deleteReason).cancellationSourceExternal(externallyTerminated).cancelActivityInstance(superExecution.getActivityInstanceId());
                    builder.execute(false, skipCustomListeners, skipIoMappings);
                    return null;
                }
            });
        }
        commandContext.getOperationLogManager().logProcessInstanceOperation("Delete", processInstanceId, null, null, Collections.singletonList(PropertyChange.EMPTY_CHANGE));
    }
    
    public void triggerHistoryEvent(final List<ProcessInstance> subProcesslist) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = configuration.getHistoryLevel();
        for (final ProcessInstance processInstance : subProcesslist) {
            if (historyLevel.isHistoryEventProduced(HistoryEventTypes.PROCESS_INSTANCE_UPDATE, processInstance)) {
                HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                    @Override
                    public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                        return producer.createProcessInstanceUpdateEvt((DelegateExecution)processInstance);
                    }
                });
            }
        }
    }
}
