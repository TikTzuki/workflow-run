// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobManager;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.impl.management.UpdateJobSuspensionStateBuilderImpl;

public abstract class AbstractSetJobStateCmd extends AbstractSetStateCmd
{
    protected String jobId;
    protected String jobDefinitionId;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processDefinitionTenantId;
    protected boolean processDefinitionTenantIdSet;
    
    public AbstractSetJobStateCmd(final UpdateJobSuspensionStateBuilderImpl builder) {
        super(false, null);
        this.processDefinitionTenantIdSet = false;
        this.jobId = builder.getJobId();
        this.jobDefinitionId = builder.getJobDefinitionId();
        this.processInstanceId = builder.getProcessInstanceId();
        this.processDefinitionId = builder.getProcessDefinitionId();
        this.processDefinitionKey = builder.getProcessDefinitionKey();
        this.processDefinitionTenantIdSet = builder.isProcessDefinitionTenantIdSet();
        this.processDefinitionTenantId = builder.getProcessDefinitionTenantId();
    }
    
    @Override
    protected void checkParameters(final CommandContext commandContext) {
        if (this.jobId == null && this.jobDefinitionId == null && this.processInstanceId == null && this.processDefinitionId == null && this.processDefinitionKey == null) {
            throw new ProcessEngineException("Job id, job definition id, process instance id, process definition id nor process definition key cannot be null");
        }
    }
    
    @Override
    protected void checkAuthorization(final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            if (this.jobId != null) {
                final JobManager jobManager = commandContext.getJobManager();
                final JobEntity job = jobManager.findJobById(this.jobId);
                if (job == null) {
                    continue;
                }
                final String processInstanceId = job.getProcessInstanceId();
                if (processInstanceId != null) {
                    checker.checkUpdateProcessInstanceById(processInstanceId);
                }
                else {
                    final String processDefinitionKey = job.getProcessDefinitionKey();
                    if (processDefinitionKey == null) {
                        continue;
                    }
                    checker.checkUpdateProcessInstanceByProcessDefinitionKey(processDefinitionKey);
                }
            }
            else if (this.jobDefinitionId != null) {
                final JobDefinitionManager jobDefinitionManager = commandContext.getJobDefinitionManager();
                final JobDefinitionEntity jobDefinition = jobDefinitionManager.findById(this.jobDefinitionId);
                if (jobDefinition == null) {
                    continue;
                }
                final String processDefinitionKey2 = jobDefinition.getProcessDefinitionKey();
                checker.checkUpdateProcessInstanceByProcessDefinitionKey(processDefinitionKey2);
            }
            else if (this.processInstanceId != null) {
                checker.checkUpdateProcessInstanceById(this.processInstanceId);
            }
            else if (this.processDefinitionId != null) {
                checker.checkUpdateProcessInstanceByProcessDefinitionId(this.processDefinitionId);
            }
            else {
                if (this.processDefinitionKey == null) {
                    continue;
                }
                checker.checkUpdateProcessInstanceByProcessDefinitionKey(this.processDefinitionKey);
            }
        }
    }
    
    @Override
    protected void updateSuspensionState(final CommandContext commandContext, final SuspensionState suspensionState) {
        final JobManager jobManager = commandContext.getJobManager();
        if (this.jobId != null) {
            jobManager.updateJobSuspensionStateById(this.jobId, suspensionState);
        }
        else if (this.jobDefinitionId != null) {
            jobManager.updateJobSuspensionStateByJobDefinitionId(this.jobDefinitionId, suspensionState);
        }
        else if (this.processInstanceId != null) {
            jobManager.updateJobSuspensionStateByProcessInstanceId(this.processInstanceId, suspensionState);
        }
        else if (this.processDefinitionId != null) {
            jobManager.updateJobSuspensionStateByProcessDefinitionId(this.processDefinitionId, suspensionState);
        }
        else if (this.processDefinitionKey != null) {
            if (!this.processDefinitionTenantIdSet) {
                jobManager.updateJobSuspensionStateByProcessDefinitionKey(this.processDefinitionKey, suspensionState);
            }
            else {
                jobManager.updateJobSuspensionStateByProcessDefinitionKeyAndTenantId(this.processDefinitionKey, this.processDefinitionTenantId, suspensionState);
            }
        }
    }
    
    @Override
    protected void logUserOperation(final CommandContext commandContext) {
        final PropertyChange propertyChange = new PropertyChange("suspensionState", null, this.getNewSuspensionState().getName());
        commandContext.getOperationLogManager().logJobOperation(this.getLogEntryOperation(), this.jobId, this.jobDefinitionId, this.processInstanceId, this.processDefinitionId, this.processDefinitionKey, propertyChange);
    }
}
