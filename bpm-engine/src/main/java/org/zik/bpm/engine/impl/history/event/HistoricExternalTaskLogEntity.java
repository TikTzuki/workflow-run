// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.history.ExternalTaskState;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricExternalTaskLog;

public class HistoricExternalTaskLogEntity extends HistoryEvent implements HistoricExternalTaskLog
{
    private static final long serialVersionUID = 1L;
    private static final String EXCEPTION_NAME = "historicExternalTaskLog.exceptionByteArray";
    protected Date timestamp;
    protected String externalTaskId;
    protected String topicName;
    protected String workerId;
    protected long priority;
    protected Integer retries;
    protected String errorMessage;
    protected String errorDetailsByteArrayId;
    protected String activityId;
    protected String activityInstanceId;
    protected String tenantId;
    protected int state;
    
    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String getExternalTaskId() {
        return this.externalTaskId;
    }
    
    public void setExternalTaskId(final String externalTaskId) {
        this.externalTaskId = externalTaskId;
    }
    
    @Override
    public String getTopicName() {
        return this.topicName;
    }
    
    public void setTopicName(final String topicName) {
        this.topicName = topicName;
    }
    
    @Override
    public String getWorkerId() {
        return this.workerId;
    }
    
    public void setWorkerId(final String workerId) {
        this.workerId = workerId;
    }
    
    @Override
    public Integer getRetries() {
        return this.retries;
    }
    
    public void setRetries(final Integer retries) {
        this.retries = retries;
    }
    
    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public void setErrorMessage(final String errorMessage) {
        if (errorMessage != null && errorMessage.length() > 666) {
            this.errorMessage = errorMessage.substring(0, 666);
        }
        else {
            this.errorMessage = errorMessage;
        }
    }
    
    public String getErrorDetailsByteArrayId() {
        return this.errorDetailsByteArrayId;
    }
    
    public void setErrorDetailsByteArrayId(final String errorDetailsByteArrayId) {
        this.errorDetailsByteArrayId = errorDetailsByteArrayId;
    }
    
    public String getErrorDetails() {
        final ByteArrayEntity byteArray = this.getErrorByteArray();
        return ExceptionUtil.getExceptionStacktrace(byteArray);
    }
    
    public void setErrorDetails(final String exception) {
        EnsureUtil.ensureNotNull("exception", (Object)exception);
        final byte[] exceptionBytes = StringUtil.toByteArray(exception);
        final ByteArrayEntity byteArray = ExceptionUtil.createExceptionByteArray("historicExternalTaskLog.exceptionByteArray", exceptionBytes, ResourceTypes.HISTORY);
        byteArray.setRootProcessInstanceId(this.rootProcessInstanceId);
        byteArray.setRemovalTime(this.removalTime);
        this.errorDetailsByteArrayId = byteArray.getId();
    }
    
    protected ByteArrayEntity getErrorByteArray() {
        if (this.errorDetailsByteArrayId != null) {
            return Context.getCommandContext().getDbEntityManager().selectById(ByteArrayEntity.class, this.errorDetailsByteArrayId);
        }
        return null;
    }
    
    @Override
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    @Override
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    public void setActivityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public long getPriority() {
        return this.priority;
    }
    
    public void setPriority(final long priority) {
        this.priority = priority;
    }
    
    public int getState() {
        return this.state;
    }
    
    public void setState(final int state) {
        this.state = state;
    }
    
    @Override
    public boolean isCreationLog() {
        return this.state == ExternalTaskState.CREATED.getStateCode();
    }
    
    @Override
    public boolean isFailureLog() {
        return this.state == ExternalTaskState.FAILED.getStateCode();
    }
    
    @Override
    public boolean isSuccessLog() {
        return this.state == ExternalTaskState.SUCCESSFUL.getStateCode();
    }
    
    @Override
    public boolean isDeletionLog() {
        return this.state == ExternalTaskState.DELETED.getStateCode();
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    @Override
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
}
