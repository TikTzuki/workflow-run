// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.jobexecutor.JobHandler;
import java.util.Date;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;

public class EverLivingJobEntity extends JobEntity
{
    private static final long serialVersionUID = 1L;
    private static final EnginePersistenceLogger LOG;
    public static final String TYPE = "ever-living";
    
    @Override
    public String getType() {
        return "ever-living";
    }
    
    @Override
    protected void postExecute(final CommandContext commandContext) {
        EverLivingJobEntity.LOG.debugJobExecuted(this);
        this.init(commandContext);
        commandContext.getHistoricJobLogManager().fireJobSuccessfulEvent(this);
    }
    
    @Override
    public void init(final CommandContext commandContext) {
        this.init(commandContext, false);
    }
    
    public void init(final CommandContext commandContext, final boolean shouldResetLock) {
        final JobHandler jobHandler = this.getJobHandler();
        if (jobHandler != null) {
            jobHandler.onDelete(this.getJobHandlerConfiguration(), this);
        }
        this.setRetries(commandContext.getProcessEngineConfiguration().getDefaultNumberOfRetries());
        if (this.exceptionByteArrayId != null) {
            this.clearFailedJobException();
        }
        if (shouldResetLock) {
            this.setLockOwner(null);
            this.setLockExpirationTime(null);
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", revision=" + this.revision + ", duedate=" + this.duedate + ", lockOwner=" + this.lockOwner + ", lockExpirationTime=" + this.lockExpirationTime + ", executionId=" + this.executionId + ", processInstanceId=" + this.processInstanceId + ", isExclusive=" + this.isExclusive + ", retries=" + this.retries + ", jobHandlerType=" + this.jobHandlerType + ", jobHandlerConfiguration=" + this.jobHandlerConfiguration + ", exceptionByteArray=" + this.exceptionByteArray + ", exceptionByteArrayId=" + this.exceptionByteArrayId + ", exceptionMessage=" + this.exceptionMessage + ", deploymentId=" + this.deploymentId + "]";
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
