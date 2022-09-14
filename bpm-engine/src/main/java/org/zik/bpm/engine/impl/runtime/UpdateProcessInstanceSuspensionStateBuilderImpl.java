// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmd.SuspendProcessInstanceCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.ActivateProcessInstanceCmd;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import org.zik.bpm.engine.impl.UpdateProcessInstancesSuspensionStateBuilderImpl;
import org.zik.bpm.engine.runtime.UpdateProcessInstancesSuspensionStateBuilder;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.runtime.UpdateProcessInstanceSuspensionStateTenantBuilder;
import org.zik.bpm.engine.runtime.UpdateProcessInstanceSuspensionStateSelectBuilder;
import org.zik.bpm.engine.runtime.UpdateProcessInstanceSuspensionStateBuilder;

public class UpdateProcessInstanceSuspensionStateBuilderImpl implements UpdateProcessInstanceSuspensionStateBuilder, UpdateProcessInstanceSuspensionStateSelectBuilder, UpdateProcessInstanceSuspensionStateTenantBuilder
{
    private static final CommandLogger LOG;
    protected final CommandExecutor commandExecutor;
    protected String processInstanceId;
    protected String processDefinitionKey;
    protected String processDefinitionId;
    protected String processDefinitionTenantId;
    protected boolean isProcessDefinitionTenantIdSet;
    
    public UpdateProcessInstanceSuspensionStateBuilderImpl(final CommandExecutor commandExecutor) {
        this.isProcessDefinitionTenantIdSet = false;
        this.commandExecutor = commandExecutor;
    }
    
    public UpdateProcessInstanceSuspensionStateBuilderImpl() {
        this(null);
    }
    
    @Override
    public UpdateProcessInstancesSuspensionStateBuilder byProcessInstanceIds(final List<String> processInstanceIds) {
        return new UpdateProcessInstancesSuspensionStateBuilderImpl(this.commandExecutor).byProcessInstanceIds(processInstanceIds);
    }
    
    @Override
    public UpdateProcessInstancesSuspensionStateBuilder byProcessInstanceIds(final String... processInstanceIds) {
        return new UpdateProcessInstancesSuspensionStateBuilderImpl(this.commandExecutor).byProcessInstanceIds(processInstanceIds);
    }
    
    @Override
    public UpdateProcessInstancesSuspensionStateBuilder byProcessInstanceQuery(final ProcessInstanceQuery processInstanceQuery) {
        return new UpdateProcessInstancesSuspensionStateBuilderImpl(this.commandExecutor).byProcessInstanceQuery(processInstanceQuery);
    }
    
    @Override
    public UpdateProcessInstancesSuspensionStateBuilder byHistoricProcessInstanceQuery(final HistoricProcessInstanceQuery historicProcessInstanceQuery) {
        return new UpdateProcessInstancesSuspensionStateBuilderImpl(this.commandExecutor).byHistoricProcessInstanceQuery(historicProcessInstanceQuery);
    }
    
    @Override
    public UpdateProcessInstanceSuspensionStateBuilderImpl byProcessInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public UpdateProcessInstanceSuspensionStateBuilderImpl byProcessDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public UpdateProcessInstanceSuspensionStateBuilderImpl byProcessDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public UpdateProcessInstanceSuspensionStateBuilderImpl processDefinitionWithoutTenantId() {
        this.processDefinitionTenantId = null;
        this.isProcessDefinitionTenantIdSet = true;
        return this;
    }
    
    @Override
    public UpdateProcessInstanceSuspensionStateBuilderImpl processDefinitionTenantId(final String tenantId) {
        EnsureUtil.ensureNotNull("tenantId", (Object)tenantId);
        this.processDefinitionTenantId = tenantId;
        this.isProcessDefinitionTenantIdSet = true;
        return this;
    }
    
    @Override
    public void activate() {
        this.validateParameters();
        final ActivateProcessInstanceCmd command = new ActivateProcessInstanceCmd(this);
        this.commandExecutor.execute((Command<Object>)command);
    }
    
    @Override
    public void suspend() {
        this.validateParameters();
        final SuspendProcessInstanceCmd command = new SuspendProcessInstanceCmd(this);
        this.commandExecutor.execute((Command<Object>)command);
    }
    
    protected void validateParameters() {
        EnsureUtil.ensureOnlyOneNotNull("Need to specify either a process instance id, a process definition id or a process definition key.", this.processInstanceId, this.processDefinitionId, this.processDefinitionKey);
        if (this.isProcessDefinitionTenantIdSet && (this.processInstanceId != null || this.processDefinitionId != null)) {
            throw UpdateProcessInstanceSuspensionStateBuilderImpl.LOG.exceptionUpdateSuspensionStateForTenantOnlyByProcessDefinitionKey();
        }
        EnsureUtil.ensureNotNull("commandExecutor", this.commandExecutor);
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessDefinitionTenantId() {
        return this.processDefinitionTenantId;
    }
    
    public boolean isProcessDefinitionTenantIdSet() {
        return this.isProcessDefinitionTenantIdSet;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
