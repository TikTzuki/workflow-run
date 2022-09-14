// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.SignalEventReceivedCmd;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import java.util.Map;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.runtime.SignalEventReceivedBuilder;

public class SignalEventReceivedBuilderImpl implements SignalEventReceivedBuilder
{
    private static final CommandLogger LOG;
    protected final CommandExecutor commandExecutor;
    protected final String signalName;
    protected String executionId;
    protected String tenantId;
    protected boolean isTenantIdSet;
    protected VariableMap variables;
    
    public SignalEventReceivedBuilderImpl(final CommandExecutor commandExecutor, final String signalName) {
        this.executionId = null;
        this.tenantId = null;
        this.isTenantIdSet = false;
        this.variables = null;
        this.commandExecutor = commandExecutor;
        this.signalName = signalName;
    }
    
    @Override
    public SignalEventReceivedBuilder setVariables(final Map<String, Object> variables) {
        if (variables != null) {
            if (this.variables == null) {
                this.variables = (VariableMap)new VariableMapImpl();
            }
            this.variables.putAll((Map)variables);
        }
        return this;
    }
    
    @Override
    public SignalEventReceivedBuilder executionId(final String executionId) {
        EnsureUtil.ensureNotNull("executionId", (Object)executionId);
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public SignalEventReceivedBuilder tenantId(final String tenantId) {
        EnsureUtil.ensureNotNull("The tenant-id cannot be null. Use 'withoutTenantId()' if you want to send the signal to a process definition or an execution which has no tenant-id.", "tenantId", tenantId);
        this.tenantId = tenantId;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public SignalEventReceivedBuilder withoutTenantId() {
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public void send() {
        if (this.executionId != null && this.isTenantIdSet) {
            throw SignalEventReceivedBuilderImpl.LOG.exceptionDeliverSignalToSingleExecutionWithTenantId();
        }
        final SignalEventReceivedCmd command = new SignalEventReceivedCmd(this);
        this.commandExecutor.execute((Command<Object>)command);
    }
    
    public String getSignalName() {
        return this.signalName;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public VariableMap getVariables() {
        return this.variables;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
