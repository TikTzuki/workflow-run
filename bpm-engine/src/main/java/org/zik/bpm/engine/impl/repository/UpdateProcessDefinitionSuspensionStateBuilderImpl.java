// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.repository;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmd.SuspendProcessDefinitionCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.ActivateProcessDefinitionCmd;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Date;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.repository.UpdateProcessDefinitionSuspensionStateTenantBuilder;
import org.zik.bpm.engine.repository.UpdateProcessDefinitionSuspensionStateSelectBuilder;
import org.zik.bpm.engine.repository.UpdateProcessDefinitionSuspensionStateBuilder;

public class UpdateProcessDefinitionSuspensionStateBuilderImpl implements UpdateProcessDefinitionSuspensionStateBuilder, UpdateProcessDefinitionSuspensionStateSelectBuilder, UpdateProcessDefinitionSuspensionStateTenantBuilder
{
    private static final CommandLogger LOG;
    protected final CommandExecutor commandExecutor;
    protected String processDefinitionKey;
    protected String processDefinitionId;
    protected boolean includeProcessInstances;
    protected Date executionDate;
    protected String processDefinitionTenantId;
    protected boolean isTenantIdSet;
    
    public UpdateProcessDefinitionSuspensionStateBuilderImpl(final CommandExecutor commandExecutor) {
        this.includeProcessInstances = false;
        this.isTenantIdSet = false;
        this.commandExecutor = commandExecutor;
    }
    
    public UpdateProcessDefinitionSuspensionStateBuilderImpl() {
        this(null);
    }
    
    @Override
    public UpdateProcessDefinitionSuspensionStateBuilderImpl byProcessDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public UpdateProcessDefinitionSuspensionStateBuilderImpl byProcessDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public UpdateProcessDefinitionSuspensionStateBuilderImpl includeProcessInstances(final boolean includeProcessInstance) {
        this.includeProcessInstances = includeProcessInstance;
        return this;
    }
    
    @Override
    public UpdateProcessDefinitionSuspensionStateBuilderImpl executionDate(final Date date) {
        this.executionDate = date;
        return this;
    }
    
    @Override
    public UpdateProcessDefinitionSuspensionStateBuilderImpl processDefinitionWithoutTenantId() {
        this.processDefinitionTenantId = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public UpdateProcessDefinitionSuspensionStateBuilderImpl processDefinitionTenantId(final String tenantId) {
        EnsureUtil.ensureNotNull("tenantId", (Object)tenantId);
        this.processDefinitionTenantId = tenantId;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public void activate() {
        this.validateParameters();
        final ActivateProcessDefinitionCmd command = new ActivateProcessDefinitionCmd(this);
        this.commandExecutor.execute((Command<Object>)command);
    }
    
    @Override
    public void suspend() {
        this.validateParameters();
        final SuspendProcessDefinitionCmd command = new SuspendProcessDefinitionCmd(this);
        this.commandExecutor.execute((Command<Object>)command);
    }
    
    protected void validateParameters() {
        EnsureUtil.ensureOnlyOneNotNull("Need to specify either a process instance id or a process definition key.", this.processDefinitionId, this.processDefinitionKey);
        if (this.processDefinitionId != null && this.isTenantIdSet) {
            throw UpdateProcessDefinitionSuspensionStateBuilderImpl.LOG.exceptionUpdateSuspensionStateForTenantOnlyByProcessDefinitionKey();
        }
        EnsureUtil.ensureNotNull("commandExecutor", this.commandExecutor);
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public boolean isIncludeProcessInstances() {
        return this.includeProcessInstances;
    }
    
    public Date getExecutionDate() {
        return this.executionDate;
    }
    
    public String getProcessDefinitionTenantId() {
        return this.processDefinitionTenantId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
