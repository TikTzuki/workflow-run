// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.impl.cmd.CorrelateAllMessageCmd;
import java.util.Arrays;
import java.util.List;
import org.zik.bpm.engine.runtime.MessageCorrelationResultWithVariables;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.CorrelateMessageCmd;
import org.zik.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import java.util.Map;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.runtime.MessageCorrelationBuilder;

public class MessageCorrelationBuilderImpl implements MessageCorrelationBuilder
{
    private static final CommandLogger LOG;
    protected CommandExecutor commandExecutor;
    protected CommandContext commandContext;
    protected boolean isExclusiveCorrelation;
    protected String messageName;
    protected String businessKey;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected VariableMap correlationProcessInstanceVariables;
    protected VariableMap correlationLocalVariables;
    protected VariableMap payloadProcessInstanceVariables;
    protected VariableMap payloadProcessInstanceVariablesLocal;
    protected String tenantId;
    protected boolean isTenantIdSet;
    protected boolean startMessagesOnly;
    protected boolean executionsOnly;
    
    public MessageCorrelationBuilderImpl(final CommandExecutor commandExecutor, final String messageName) {
        this(messageName);
        EnsureUtil.ensureNotNull("commandExecutor", commandExecutor);
        this.commandExecutor = commandExecutor;
    }
    
    public MessageCorrelationBuilderImpl(final CommandContext commandContext, final String messageName) {
        this(messageName);
        EnsureUtil.ensureNotNull("commandContext", commandContext);
        this.commandContext = commandContext;
    }
    
    private MessageCorrelationBuilderImpl(final String messageName) {
        this.isExclusiveCorrelation = false;
        this.tenantId = null;
        this.isTenantIdSet = false;
        this.startMessagesOnly = false;
        this.executionsOnly = false;
        this.messageName = messageName;
    }
    
    @Override
    public MessageCorrelationBuilder processInstanceBusinessKey(final String businessKey) {
        EnsureUtil.ensureNotNull("businessKey", (Object)businessKey);
        this.businessKey = businessKey;
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder processInstanceVariableEquals(final String variableName, final Object variableValue) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        this.ensureCorrelationProcessInstanceVariablesInitialized();
        this.correlationProcessInstanceVariables.put((Object)variableName, variableValue);
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder processInstanceVariablesEqual(final Map<String, Object> variables) {
        EnsureUtil.ensureNotNull("variables", variables);
        this.ensureCorrelationProcessInstanceVariablesInitialized();
        this.correlationProcessInstanceVariables.putAll((Map)variables);
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder localVariableEquals(final String variableName, final Object variableValue) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        this.ensureCorrelationLocalVariablesInitialized();
        this.correlationLocalVariables.put((Object)variableName, variableValue);
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder localVariablesEqual(final Map<String, Object> variables) {
        EnsureUtil.ensureNotNull("variables", variables);
        this.ensureCorrelationLocalVariablesInitialized();
        this.correlationLocalVariables.putAll((Map)variables);
        return this;
    }
    
    protected void ensureCorrelationProcessInstanceVariablesInitialized() {
        if (this.correlationProcessInstanceVariables == null) {
            this.correlationProcessInstanceVariables = (VariableMap)new VariableMapImpl();
        }
    }
    
    protected void ensureCorrelationLocalVariablesInitialized() {
        if (this.correlationLocalVariables == null) {
            this.correlationLocalVariables = (VariableMap)new VariableMapImpl();
        }
    }
    
    @Override
    public MessageCorrelationBuilder processInstanceId(final String id) {
        EnsureUtil.ensureNotNull("processInstanceId", (Object)id);
        this.processInstanceId = id;
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder setVariable(final String variableName, final Object variableValue) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        this.ensurePayloadProcessInstanceVariablesInitialized();
        this.payloadProcessInstanceVariables.put((Object)variableName, variableValue);
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder setVariableLocal(final String variableName, final Object variableValue) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        this.ensurePayloadProcessInstanceVariablesLocalInitialized();
        this.payloadProcessInstanceVariablesLocal.put((Object)variableName, variableValue);
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder setVariables(final Map<String, Object> variables) {
        if (variables != null) {
            this.ensurePayloadProcessInstanceVariablesInitialized();
            this.payloadProcessInstanceVariables.putAll((Map)variables);
        }
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder setVariablesLocal(final Map<String, Object> variables) {
        if (variables != null) {
            this.ensurePayloadProcessInstanceVariablesLocalInitialized();
            this.payloadProcessInstanceVariablesLocal.putAll((Map)variables);
        }
        return this;
    }
    
    protected void ensurePayloadProcessInstanceVariablesInitialized() {
        if (this.payloadProcessInstanceVariables == null) {
            this.payloadProcessInstanceVariables = (VariableMap)new VariableMapImpl();
        }
    }
    
    protected void ensurePayloadProcessInstanceVariablesLocalInitialized() {
        if (this.payloadProcessInstanceVariablesLocal == null) {
            this.payloadProcessInstanceVariablesLocal = (VariableMap)new VariableMapImpl();
        }
    }
    
    @Override
    public MessageCorrelationBuilder tenantId(final String tenantId) {
        EnsureUtil.ensureNotNull("The tenant-id cannot be null. Use 'withoutTenantId()' if you want to correlate the message to a process definition or an execution which has no tenant-id.", "tenantId", tenantId);
        this.isTenantIdSet = true;
        this.tenantId = tenantId;
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantId = null;
        return this;
    }
    
    @Override
    public MessageCorrelationBuilder startMessageOnly() {
        EnsureUtil.ensureFalse("Either startMessageOnly or executionsOnly can be set", this.executionsOnly);
        this.startMessagesOnly = true;
        return this;
    }
    
    public MessageCorrelationBuilder executionsOnly() {
        EnsureUtil.ensureFalse("Either startMessageOnly or executionsOnly can be set", this.startMessagesOnly);
        this.executionsOnly = true;
        return this;
    }
    
    @Override
    public void correlate() {
        this.correlateWithResult();
    }
    
    @Override
    public MessageCorrelationResult correlateWithResult() {
        if (this.startMessagesOnly) {
            this.ensureCorrelationVariablesNotSet();
            this.ensureProcessDefinitionAndTenantIdNotSet();
        }
        else {
            this.ensureProcessDefinitionIdNotSet();
            this.ensureProcessInstanceAndTenantIdNotSet();
        }
        return this.execute((Command<MessageCorrelationResult>)new CorrelateMessageCmd(this, false, false, this.startMessagesOnly));
    }
    
    @Override
    public MessageCorrelationResultWithVariables correlateWithResultAndVariables(final boolean deserializeValues) {
        if (this.startMessagesOnly) {
            this.ensureCorrelationVariablesNotSet();
            this.ensureProcessDefinitionAndTenantIdNotSet();
        }
        else {
            this.ensureProcessDefinitionIdNotSet();
            this.ensureProcessInstanceAndTenantIdNotSet();
        }
        return this.execute((Command<MessageCorrelationResultWithVariables>)new CorrelateMessageCmd(this, true, deserializeValues, this.startMessagesOnly));
    }
    
    @Override
    public void correlateExclusively() {
        this.isExclusiveCorrelation = true;
        this.correlate();
    }
    
    @Override
    public void correlateAll() {
        this.correlateAllWithResult();
    }
    
    @Override
    public List<MessageCorrelationResult> correlateAllWithResult() {
        if (this.startMessagesOnly) {
            this.ensureCorrelationVariablesNotSet();
            this.ensureProcessDefinitionAndTenantIdNotSet();
            final MessageCorrelationResult result = this.execute((Command<MessageCorrelationResult>)new CorrelateMessageCmd(this, false, false, this.startMessagesOnly));
            return Arrays.asList(result);
        }
        this.ensureProcessDefinitionIdNotSet();
        this.ensureProcessInstanceAndTenantIdNotSet();
        return this.execute((Command<List<MessageCorrelationResult>>)new CorrelateAllMessageCmd(this, false, false));
    }
    
    @Override
    public List<MessageCorrelationResultWithVariables> correlateAllWithResultAndVariables(final boolean deserializeValues) {
        if (this.startMessagesOnly) {
            this.ensureCorrelationVariablesNotSet();
            this.ensureProcessDefinitionAndTenantIdNotSet();
            final MessageCorrelationResultWithVariables result = this.execute((Command<MessageCorrelationResultWithVariables>)new CorrelateMessageCmd(this, true, deserializeValues, this.startMessagesOnly));
            return Arrays.asList(result);
        }
        this.ensureProcessDefinitionIdNotSet();
        this.ensureProcessInstanceAndTenantIdNotSet();
        return this.execute((Command<List<MessageCorrelationResultWithVariables>>)new CorrelateAllMessageCmd(this, true, deserializeValues));
    }
    
    @Override
    public ProcessInstance correlateStartMessage() {
        this.startMessageOnly();
        final MessageCorrelationResult result = this.correlateWithResult();
        return result.getProcessInstance();
    }
    
    protected void ensureProcessDefinitionIdNotSet() {
        if (this.processDefinitionId != null) {
            throw MessageCorrelationBuilderImpl.LOG.exceptionCorrelateMessageWithProcessDefinitionId();
        }
    }
    
    protected void ensureProcessInstanceAndTenantIdNotSet() {
        if (this.processInstanceId != null && this.isTenantIdSet) {
            throw MessageCorrelationBuilderImpl.LOG.exceptionCorrelateMessageWithProcessInstanceAndTenantId();
        }
    }
    
    protected void ensureCorrelationVariablesNotSet() {
        if (this.correlationProcessInstanceVariables != null || this.correlationLocalVariables != null) {
            throw MessageCorrelationBuilderImpl.LOG.exceptionCorrelateStartMessageWithCorrelationVariables();
        }
    }
    
    protected void ensureProcessDefinitionAndTenantIdNotSet() {
        if (this.processDefinitionId != null && this.isTenantIdSet) {
            throw MessageCorrelationBuilderImpl.LOG.exceptionCorrelateMessageWithProcessDefinitionAndTenantId();
        }
    }
    
    protected <T> T execute(final Command<T> command) {
        if (this.commandExecutor != null) {
            return this.commandExecutor.execute(command);
        }
        return command.execute(this.commandContext);
    }
    
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }
    
    public CommandContext getCommandContext() {
        return this.commandContext;
    }
    
    public String getMessageName() {
        return this.messageName;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public Map<String, Object> getCorrelationProcessInstanceVariables() {
        return (Map<String, Object>)this.correlationProcessInstanceVariables;
    }
    
    public Map<String, Object> getCorrelationLocalVariables() {
        return (Map<String, Object>)this.correlationLocalVariables;
    }
    
    public Map<String, Object> getPayloadProcessInstanceVariables() {
        return (Map<String, Object>)this.payloadProcessInstanceVariables;
    }
    
    public VariableMap getPayloadProcessInstanceVariablesLocal() {
        return this.payloadProcessInstanceVariablesLocal;
    }
    
    public boolean isExclusiveCorrelation() {
        return this.isExclusiveCorrelation;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public boolean isExecutionsOnly() {
        return this.executionsOnly;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
