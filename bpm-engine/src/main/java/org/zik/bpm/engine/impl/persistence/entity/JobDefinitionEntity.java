// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.management.JobDefinition;

public class JobDefinitionEntity implements JobDefinition, HasDbRevision, HasDbReferences, DbEntity, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int revision;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String activityId;
    protected String jobType;
    protected String jobConfiguration;
    protected int suspensionState;
    protected Long jobPriority;
    protected String tenantId;
    protected String deploymentId;
    
    public JobDefinitionEntity() {
        this.suspensionState = SuspensionState.ACTIVE.getStateCode();
    }
    
    public JobDefinitionEntity(final JobDeclaration<?, ?> jobDeclaration) {
        this.suspensionState = SuspensionState.ACTIVE.getStateCode();
        this.activityId = jobDeclaration.getActivityId();
        this.jobConfiguration = jobDeclaration.getJobConfiguration();
        this.jobType = jobDeclaration.getJobHandlerType();
    }
    
    @Override
    public Object getPersistentState() {
        final HashMap<String, Object> state = new HashMap<String, Object>();
        state.put("processDefinitionId", this.processDefinitionId);
        state.put("processDefinitionKey", this.processDefinitionKey);
        state.put("activityId", this.activityId);
        state.put("jobType", this.jobType);
        state.put("jobConfiguration", this.jobConfiguration);
        state.put("suspensionState", this.suspensionState);
        state.put("jobPriority", this.jobPriority);
        state.put("tenantId", this.tenantId);
        state.put("deploymentId", this.deploymentId);
        return state;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public boolean isSuspended() {
        return SuspensionState.SUSPENDED.getStateCode() == this.suspensionState;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    @Override
    public String getJobType() {
        return this.jobType;
    }
    
    public void setJobType(final String jobType) {
        this.jobType = jobType;
    }
    
    @Override
    public String getJobConfiguration() {
        return this.jobConfiguration;
    }
    
    public void setJobConfiguration(final String jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
    }
    
    @Override
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    public int getSuspensionState() {
        return this.suspensionState;
    }
    
    public void setSuspensionState(final int state) {
        this.suspensionState = state;
    }
    
    @Override
    public Long getOverridingJobPriority() {
        return this.jobPriority;
    }
    
    public void setJobPriority(final Long jobPriority) {
        this.jobPriority = jobPriority;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        return referenceIdAndClass;
    }
}
