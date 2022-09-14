// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.io.Serializable;
import org.zik.bpm.engine.management.JobDefinition;
import org.zik.bpm.engine.management.JobDefinitionQuery;

public class JobDefinitionQueryImpl extends AbstractQuery<JobDefinitionQuery, JobDefinition> implements JobDefinitionQuery, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String[] activityIds;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String jobType;
    protected String jobConfiguration;
    protected SuspensionState suspensionState;
    protected Boolean withOverridingJobPriority;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected boolean includeJobDefinitionsWithoutTenantId;
    
    public JobDefinitionQueryImpl() {
        this.isTenantIdSet = false;
        this.includeJobDefinitionsWithoutTenantId = false;
    }
    
    public JobDefinitionQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
        this.includeJobDefinitionsWithoutTenantId = false;
    }
    
    @Override
    public JobDefinitionQuery jobDefinitionId(final String jobDefinitionId) {
        EnsureUtil.ensureNotNull("Job definition id", (Object)jobDefinitionId);
        this.id = jobDefinitionId;
        return this;
    }
    
    @Override
    public JobDefinitionQuery activityIdIn(final String... activityIds) {
        EnsureUtil.ensureNotNull("Activity ids", (Object[])activityIds);
        this.activityIds = activityIds;
        return this;
    }
    
    @Override
    public JobDefinitionQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("Process definition id", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public JobDefinitionQuery processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("Process definition key", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public JobDefinitionQuery jobType(final String jobType) {
        EnsureUtil.ensureNotNull("Job type", (Object)jobType);
        this.jobType = jobType;
        return this;
    }
    
    @Override
    public JobDefinitionQuery jobConfiguration(final String jobConfiguration) {
        EnsureUtil.ensureNotNull("Job configuration", (Object)jobConfiguration);
        this.jobConfiguration = jobConfiguration;
        return this;
    }
    
    @Override
    public JobDefinitionQuery active() {
        this.suspensionState = SuspensionState.ACTIVE;
        return this;
    }
    
    @Override
    public JobDefinitionQuery suspended() {
        this.suspensionState = SuspensionState.SUSPENDED;
        return this;
    }
    
    @Override
    public JobDefinitionQuery withOverridingJobPriority() {
        this.withOverridingJobPriority = true;
        return this;
    }
    
    @Override
    public JobDefinitionQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public JobDefinitionQuery withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantIds = null;
        return this;
    }
    
    @Override
    public JobDefinitionQuery includeJobDefinitionsWithoutTenantId() {
        this.includeJobDefinitionsWithoutTenantId = true;
        return this;
    }
    
    @Override
    public JobDefinitionQuery orderByJobDefinitionId() {
        return ((AbstractQuery<JobDefinitionQuery, U>)this).orderBy(JobDefinitionQueryProperty.JOB_DEFINITION_ID);
    }
    
    @Override
    public JobDefinitionQuery orderByActivityId() {
        return ((AbstractQuery<JobDefinitionQuery, U>)this).orderBy(JobDefinitionQueryProperty.ACTIVITY_ID);
    }
    
    @Override
    public JobDefinitionQuery orderByProcessDefinitionId() {
        return ((AbstractQuery<JobDefinitionQuery, U>)this).orderBy(JobDefinitionQueryProperty.PROCESS_DEFINITION_ID);
    }
    
    @Override
    public JobDefinitionQuery orderByProcessDefinitionKey() {
        return ((AbstractQuery<JobDefinitionQuery, U>)this).orderBy(JobDefinitionQueryProperty.PROCESS_DEFINITION_KEY);
    }
    
    @Override
    public JobDefinitionQuery orderByJobType() {
        return ((AbstractQuery<JobDefinitionQuery, U>)this).orderBy(JobDefinitionQueryProperty.JOB_TYPE);
    }
    
    @Override
    public JobDefinitionQuery orderByJobConfiguration() {
        return ((AbstractQuery<JobDefinitionQuery, U>)this).orderBy(JobDefinitionQueryProperty.JOB_CONFIGURATION);
    }
    
    @Override
    public JobDefinitionQuery orderByTenantId() {
        return ((AbstractQuery<JobDefinitionQuery, U>)this).orderBy(JobDefinitionQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getJobDefinitionManager().findJobDefinitionCountByQueryCriteria(this);
    }
    
    @Override
    public List<JobDefinition> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getJobDefinitionManager().findJobDefnitionByQueryCriteria(this, page);
    }
    
    public String getId() {
        return this.id;
    }
    
    public String[] getActivityIds() {
        return this.activityIds;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String getJobType() {
        return this.jobType;
    }
    
    public String getJobConfiguration() {
        return this.jobConfiguration;
    }
    
    public SuspensionState getSuspensionState() {
        return this.suspensionState;
    }
    
    public Boolean getWithOverridingJobPriority() {
        return this.withOverridingJobPriority;
    }
}
