// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.camunda.bpm.engine.variable.Variables;
import org.zik.bpm.engine.impl.runtime.MessageCorrelationResultImpl;
import java.util.Iterator;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.runtime.MessageCorrelationResultType;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.runtime.ProcessInstance;
import java.util.Map;
import org.zik.bpm.engine.impl.runtime.CorrelationHandlerResult;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionVariableSnapshotObserver;
import org.zik.bpm.engine.impl.MessageCorrelationBuilderImpl;

public abstract class AbstractCorrelateMessageCmd
{
    protected final String messageName;
    protected final MessageCorrelationBuilderImpl builder;
    protected ExecutionVariableSnapshotObserver variablesListener;
    protected boolean variablesEnabled;
    protected boolean deserializeVariableValues;
    
    protected AbstractCorrelateMessageCmd(final MessageCorrelationBuilderImpl builder) {
        this.variablesEnabled = false;
        this.deserializeVariableValues = false;
        this.builder = builder;
        this.messageName = builder.getMessageName();
    }
    
    protected AbstractCorrelateMessageCmd(final MessageCorrelationBuilderImpl builder, final boolean variablesEnabled, final boolean deserializeVariableValues) {
        this(builder);
        this.variablesEnabled = variablesEnabled;
        this.deserializeVariableValues = deserializeVariableValues;
    }
    
    protected void triggerExecution(final CommandContext commandContext, final CorrelationHandlerResult correlationResult) {
        final String executionId = correlationResult.getExecutionEntity().getId();
        final MessageEventReceivedCmd command = new MessageEventReceivedCmd(this.messageName, executionId, this.builder.getPayloadProcessInstanceVariables(), (Map<String, Object>)this.builder.getPayloadProcessInstanceVariablesLocal(), this.builder.isExclusiveCorrelation());
        command.execute(commandContext);
    }
    
    protected ProcessInstance instantiateProcess(final CommandContext commandContext, final CorrelationHandlerResult correlationResult) {
        final ProcessDefinitionEntity processDefinitionEntity = correlationResult.getProcessDefinitionEntity();
        final ActivityImpl messageStartEvent = processDefinitionEntity.findActivity(correlationResult.getStartEventActivityId());
        final ExecutionEntity processInstance = processDefinitionEntity.createProcessInstance(this.builder.getBusinessKey(), messageStartEvent);
        if (this.variablesEnabled) {
            this.variablesListener = new ExecutionVariableSnapshotObserver(processInstance, false, this.deserializeVariableValues);
        }
        final VariableMap startVariables = this.resolveStartVariables();
        processInstance.start((Map<String, Object>)startVariables);
        return processInstance;
    }
    
    protected void checkAuthorization(final CorrelationHandlerResult correlation) {
        final CommandContext commandContext = Context.getCommandContext();
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            if (MessageCorrelationResultType.Execution.equals(correlation.getResultType())) {
                final ExecutionEntity execution = correlation.getExecutionEntity();
                checker.checkUpdateProcessInstanceById(execution.getProcessInstanceId());
            }
            else {
                final ProcessDefinitionEntity definition = correlation.getProcessDefinitionEntity();
                checker.checkCreateProcessInstance(definition);
            }
        }
    }
    
    protected MessageCorrelationResultImpl createMessageCorrelationResult(final CommandContext commandContext, final CorrelationHandlerResult handlerResult) {
        final MessageCorrelationResultImpl resultWithVariables = new MessageCorrelationResultImpl(handlerResult);
        if (MessageCorrelationResultType.Execution.equals(handlerResult.getResultType())) {
            final ExecutionEntity execution = this.findProcessInstanceExecution(commandContext, handlerResult);
            if (this.variablesEnabled && execution != null) {
                this.variablesListener = new ExecutionVariableSnapshotObserver(execution, false, this.deserializeVariableValues);
            }
            this.triggerExecution(commandContext, handlerResult);
        }
        else {
            final ProcessInstance instance = this.instantiateProcess(commandContext, handlerResult);
            resultWithVariables.setProcessInstance(instance);
        }
        if (this.variablesListener != null) {
            resultWithVariables.setVariables(this.variablesListener.getVariables());
        }
        return resultWithVariables;
    }
    
    protected ExecutionEntity findProcessInstanceExecution(final CommandContext commandContext, final CorrelationHandlerResult handlerResult) {
        final ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(handlerResult.getExecution().getProcessInstanceId());
        return execution;
    }
    
    protected VariableMap resolveStartVariables() {
        final VariableMap mergedVariables = Variables.createVariables();
        mergedVariables.putAll((Map)this.builder.getPayloadProcessInstanceVariables());
        mergedVariables.putAll((Map)this.builder.getPayloadProcessInstanceVariablesLocal());
        return mergedVariables;
    }
}
