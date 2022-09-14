// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.batch.CorrelateAllMessageBatchCmd;
import org.zik.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.runtime.MessageCorrelationAsyncBuilder;

public class MessageCorrelationAsyncBuilderImpl implements MessageCorrelationAsyncBuilder
{
    protected CommandExecutor commandExecutor;
    protected String messageName;
    protected Map<String, Object> payloadProcessInstanceVariables;
    protected List<String> processInstanceIds;
    protected ProcessInstanceQuery processInstanceQuery;
    protected HistoricProcessInstanceQuery historicProcessInstanceQuery;
    
    public MessageCorrelationAsyncBuilderImpl(final CommandExecutor commandExecutor, final String messageName) {
        this(messageName);
        EnsureUtil.ensureNotNull("commandExecutor", commandExecutor);
        this.commandExecutor = commandExecutor;
    }
    
    private MessageCorrelationAsyncBuilderImpl(final String messageName) {
        this.messageName = messageName;
    }
    
    @Override
    public MessageCorrelationAsyncBuilder processInstanceIds(final List<String> ids) {
        EnsureUtil.ensureNotNull("processInstanceIds", ids);
        this.processInstanceIds = ids;
        return this;
    }
    
    @Override
    public MessageCorrelationAsyncBuilder processInstanceQuery(final ProcessInstanceQuery processInstanceQuery) {
        EnsureUtil.ensureNotNull("processInstanceQuery", processInstanceQuery);
        this.processInstanceQuery = processInstanceQuery;
        return this;
    }
    
    @Override
    public MessageCorrelationAsyncBuilder historicProcessInstanceQuery(final HistoricProcessInstanceQuery historicProcessInstanceQuery) {
        EnsureUtil.ensureNotNull("historicProcessInstanceQuery", historicProcessInstanceQuery);
        this.historicProcessInstanceQuery = historicProcessInstanceQuery;
        return this;
    }
    
    @Override
    public MessageCorrelationAsyncBuilder setVariable(final String variableName, final Object variableValue) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        this.ensurePayloadProcessInstanceVariablesInitialized();
        this.payloadProcessInstanceVariables.put(variableName, variableValue);
        return this;
    }
    
    @Override
    public MessageCorrelationAsyncBuilder setVariables(final Map<String, Object> variables) {
        if (variables != null) {
            this.ensurePayloadProcessInstanceVariablesInitialized();
            this.payloadProcessInstanceVariables.putAll(variables);
        }
        return this;
    }
    
    protected void ensurePayloadProcessInstanceVariablesInitialized() {
        if (this.payloadProcessInstanceVariables == null) {
            this.payloadProcessInstanceVariables = (Map<String, Object>)new VariableMapImpl();
        }
    }
    
    @Override
    public Batch correlateAllAsync() {
        return this.commandExecutor.execute((Command<Batch>)new CorrelateAllMessageBatchCmd(this));
    }
    
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }
    
    public String getMessageName() {
        return this.messageName;
    }
    
    public List<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public ProcessInstanceQuery getProcessInstanceQuery() {
        return this.processInstanceQuery;
    }
    
    public HistoricProcessInstanceQuery getHistoricProcessInstanceQuery() {
        return this.historicProcessInstanceQuery;
    }
    
    public Map<String, Object> getPayloadProcessInstanceVariables() {
        return this.payloadProcessInstanceVariables;
    }
}
