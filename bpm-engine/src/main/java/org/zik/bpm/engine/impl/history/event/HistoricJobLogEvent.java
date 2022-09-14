// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.history.JobState;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import org.zik.bpm.engine.impl.util.StringUtil;
import java.util.Date;

public class HistoricJobLogEvent extends HistoryEvent
{
    private static final long serialVersionUID = 1L;
    protected Date timestamp;
    protected String jobId;
    protected Date jobDueDate;
    protected int jobRetries;
    protected long jobPriority;
    protected String jobExceptionMessage;
    protected String exceptionByteArrayId;
    protected String jobDefinitionId;
    protected String jobDefinitionType;
    protected String jobDefinitionConfiguration;
    protected String activityId;
    protected String failedActivityId;
    protected String deploymentId;
    protected int state;
    protected String tenantId;
    protected String hostname;
    
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getJobId() {
        return this.jobId;
    }
    
    public void setJobId(final String jobId) {
        this.jobId = jobId;
    }
    
    public Date getJobDueDate() {
        return this.jobDueDate;
    }
    
    public void setJobDueDate(final Date jobDueDate) {
        this.jobDueDate = jobDueDate;
    }
    
    public int getJobRetries() {
        return this.jobRetries;
    }
    
    public void setJobRetries(final int jobRetries) {
        this.jobRetries = jobRetries;
    }
    
    public long getJobPriority() {
        return this.jobPriority;
    }
    
    public void setJobPriority(final long jobPriority) {
        this.jobPriority = jobPriority;
    }
    
    public String getJobExceptionMessage() {
        return this.jobExceptionMessage;
    }
    
    public void setJobExceptionMessage(final String jobExceptionMessage) {
        this.jobExceptionMessage = StringUtil.trimToMaximumLengthAllowed(jobExceptionMessage);
    }
    
    public String getExceptionByteArrayId() {
        return this.exceptionByteArrayId;
    }
    
    public void setExceptionByteArrayId(final String exceptionByteArrayId) {
        this.exceptionByteArrayId = exceptionByteArrayId;
    }
    
    public String getExceptionStacktrace() {
        final ByteArrayEntity byteArray = this.getExceptionByteArray();
        return ExceptionUtil.getExceptionStacktrace(byteArray);
    }
    
    protected ByteArrayEntity getExceptionByteArray() {
        if (this.exceptionByteArrayId != null) {
            return Context.getCommandContext().getDbEntityManager().selectById(ByteArrayEntity.class, this.exceptionByteArrayId);
        }
        return null;
    }
    
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    public void setJobDefinitionId(final String jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
    }
    
    public String getJobDefinitionType() {
        return this.jobDefinitionType;
    }
    
    public void setJobDefinitionType(final String jobDefinitionType) {
        this.jobDefinitionType = jobDefinitionType;
    }
    
    public String getJobDefinitionConfiguration() {
        return this.jobDefinitionConfiguration;
    }
    
    public void setJobDefinitionConfiguration(final String jobDefinitionConfiguration) {
        this.jobDefinitionConfiguration = jobDefinitionConfiguration;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public int getState() {
        return this.state;
    }
    
    public void setState(final int state) {
        this.state = state;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getHostname() {
        return this.hostname;
    }
    
    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }
    
    public boolean isCreationLog() {
        return this.state == JobState.CREATED.getStateCode();
    }
    
    public boolean isFailureLog() {
        return this.state == JobState.FAILED.getStateCode();
    }
    
    public boolean isSuccessLog() {
        return this.state == JobState.SUCCESSFUL.getStateCode();
    }
    
    public boolean isDeletionLog() {
        return this.state == JobState.DELETED.getStateCode();
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    @Override
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    public String getFailedActivityId() {
        return this.failedActivityId;
    }
    
    public void setFailedActivityId(final String failedActivityId) {
        this.failedActivityId = failedActivityId;
    }
}
