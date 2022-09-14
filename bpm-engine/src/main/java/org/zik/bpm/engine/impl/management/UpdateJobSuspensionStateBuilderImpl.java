// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.management;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmd.SuspendJobCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.ActivateJobCmd;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.management.UpdateJobSuspensionStateTenantBuilder;
import org.zik.bpm.engine.management.UpdateJobSuspensionStateSelectBuilder;
import org.zik.bpm.engine.management.UpdateJobSuspensionStateBuilder;

public class UpdateJobSuspensionStateBuilderImpl implements UpdateJobSuspensionStateBuilder, UpdateJobSuspensionStateSelectBuilder, UpdateJobSuspensionStateTenantBuilder
{
    private static final CommandLogger LOG;
    protected final CommandExecutor commandExecutor;
    protected String jobId;
    protected String jobDefinitionId;
    protected String processInstanceId;
    protected String processDefinitionKey;
    protected String processDefinitionId;
    protected String processDefinitionTenantId;
    protected boolean isProcessDefinitionTenantIdSet;
    
    public UpdateJobSuspensionStateBuilderImpl(final CommandExecutor commandExecutor) {
        this.isProcessDefinitionTenantIdSet = false;
        this.commandExecutor = commandExecutor;
    }
    
    public UpdateJobSuspensionStateBuilderImpl() {
        this(null);
    }
    
    @Override
    public UpdateJobSuspensionStateBuilderImpl byJobId(final String jobId) {
        EnsureUtil.ensureNotNull("jobId", (Object)jobId);
        this.jobId = jobId;
        return this;
    }
    
    @Override
    public UpdateJobSuspensionStateBuilderImpl byJobDefinitionId(final String jobDefinitionId) {
        EnsureUtil.ensureNotNull("jobDefinitionId", (Object)jobDefinitionId);
        this.jobDefinitionId = jobDefinitionId;
        return this;
    }
    
    @Override
    public UpdateJobSuspensionStateBuilderImpl byProcessInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public UpdateJobSuspensionStateBuilderImpl byProcessDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public UpdateJobSuspensionStateBuilderImpl byProcessDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public UpdateJobSuspensionStateBuilderImpl processDefinitionWithoutTenantId() {
        this.processDefinitionTenantId = null;
        this.isProcessDefinitionTenantIdSet = true;
        return this;
    }
    
    @Override
    public UpdateJobSuspensionStateBuilderImpl processDefinitionTenantId(final String tenantId) {
        EnsureUtil.ensureNotNull("tenantId", (Object)tenantId);
        this.processDefinitionTenantId = tenantId;
        this.isProcessDefinitionTenantIdSet = true;
        return this;
    }
    
    @Override
    public void activate() {
        this.validateParameters();
        final ActivateJobCmd command = new ActivateJobCmd(this);
        this.commandExecutor.execute((Command<Object>)command);
    }
    
    @Override
    public void suspend() {
        this.validateParameters();
        final SuspendJobCmd command = new SuspendJobCmd(this);
        this.commandExecutor.execute((Command<Object>)command);
    }
    
    protected void validateParameters() {
        EnsureUtil.ensureOnlyOneNotNull("Need to specify either a job id, a job definition id, a process instance id, a process definition id or a process definition key.", this.jobId, this.jobDefinitionId, this.processInstanceId, this.processDefinitionId, this.processDefinitionKey);
        if (this.isProcessDefinitionTenantIdSet && (this.jobId != null || this.jobDefinitionId != null || this.processInstanceId != null || this.processDefinitionId != null)) {
            throw UpdateJobSuspensionStateBuilderImpl.LOG.exceptionUpdateSuspensionStateForTenantOnlyByProcessDefinitionKey();
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
    
    public String getJobId() {
        return this.jobId;
    }
    
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
