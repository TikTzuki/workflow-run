// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.management;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmd.SuspendJobDefinitionCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.ActivateJobDefinitionCmd;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Date;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.management.UpdateJobDefinitionSuspensionStateTenantBuilder;
import org.zik.bpm.engine.management.UpdateJobDefinitionSuspensionStateSelectBuilder;
import org.zik.bpm.engine.management.UpdateJobDefinitionSuspensionStateBuilder;

public class UpdateJobDefinitionSuspensionStateBuilderImpl implements UpdateJobDefinitionSuspensionStateBuilder, UpdateJobDefinitionSuspensionStateSelectBuilder, UpdateJobDefinitionSuspensionStateTenantBuilder
{
    private static final CommandLogger LOG;
    protected final CommandExecutor commandExecutor;
    protected String jobDefinitionId;
    protected String processDefinitionKey;
    protected String processDefinitionId;
    protected String processDefinitionTenantId;
    protected boolean isProcessDefinitionTenantIdSet;
    protected boolean includeJobs;
    protected Date executionDate;
    
    public UpdateJobDefinitionSuspensionStateBuilderImpl(final CommandExecutor commandExecutor) {
        this.isProcessDefinitionTenantIdSet = false;
        this.includeJobs = false;
        this.commandExecutor = commandExecutor;
    }
    
    public UpdateJobDefinitionSuspensionStateBuilderImpl() {
        this(null);
    }
    
    @Override
    public UpdateJobDefinitionSuspensionStateBuilderImpl byJobDefinitionId(final String jobDefinitionId) {
        EnsureUtil.ensureNotNull("jobDefinitionId", (Object)jobDefinitionId);
        this.jobDefinitionId = jobDefinitionId;
        return this;
    }
    
    @Override
    public UpdateJobDefinitionSuspensionStateBuilderImpl byProcessDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public UpdateJobDefinitionSuspensionStateBuilderImpl byProcessDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public UpdateJobDefinitionSuspensionStateBuilderImpl processDefinitionWithoutTenantId() {
        this.processDefinitionTenantId = null;
        this.isProcessDefinitionTenantIdSet = true;
        return this;
    }
    
    @Override
    public UpdateJobDefinitionSuspensionStateBuilderImpl processDefinitionTenantId(final String tenantId) {
        EnsureUtil.ensureNotNull("tenantId", (Object)tenantId);
        this.processDefinitionTenantId = tenantId;
        this.isProcessDefinitionTenantIdSet = true;
        return this;
    }
    
    @Override
    public UpdateJobDefinitionSuspensionStateBuilderImpl includeJobs(final boolean includeJobs) {
        this.includeJobs = includeJobs;
        return this;
    }
    
    @Override
    public UpdateJobDefinitionSuspensionStateBuilderImpl executionDate(final Date executionDate) {
        this.executionDate = executionDate;
        return this;
    }
    
    @Override
    public void activate() {
        this.validateParameters();
        final ActivateJobDefinitionCmd command = new ActivateJobDefinitionCmd(this);
        this.commandExecutor.execute((Command<Object>)command);
    }
    
    @Override
    public void suspend() {
        this.validateParameters();
        final SuspendJobDefinitionCmd command = new SuspendJobDefinitionCmd(this);
        this.commandExecutor.execute((Command<Object>)command);
    }
    
    protected void validateParameters() {
        EnsureUtil.ensureOnlyOneNotNull("Need to specify either a job definition id, a process definition id or a process definition key.", this.jobDefinitionId, this.processDefinitionId, this.processDefinitionKey);
        if (this.isProcessDefinitionTenantIdSet && (this.jobDefinitionId != null || this.processDefinitionId != null)) {
            throw UpdateJobDefinitionSuspensionStateBuilderImpl.LOG.exceptionUpdateSuspensionStateForTenantOnlyByProcessDefinitionKey();
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
    
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    public boolean isIncludeJobs() {
        return this.includeJobs;
    }
    
    public Date getExecutionDate() {
        return this.executionDate;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
