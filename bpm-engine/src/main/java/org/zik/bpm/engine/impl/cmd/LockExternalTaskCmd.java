// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import java.util.Date;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;

public class LockExternalTaskCmd extends HandleExternalTaskCmd
{
    protected long lockDuration;
    
    public LockExternalTaskCmd(final String externalTaskId, final String workerId, final long lockDuration) {
        super(externalTaskId, workerId);
        this.lockDuration = lockDuration;
    }
    
    @Override
    protected void execute(final ExternalTaskEntity externalTask) {
        externalTask.lock(this.workerId, this.lockDuration);
    }
    
    @Override
    public String getErrorMessageOnWrongWorkerAccess() {
        return "External Task " + this.externalTaskId + " cannot be locked by worker '" + this.workerId;
    }
    
    @Override
    protected boolean validateWorkerViolation(final ExternalTaskEntity externalTask) {
        final String existingWorkerId = externalTask.getWorkerId();
        final Date existingLockExpirationTime = externalTask.getLockExpirationTime();
        final boolean workerValidation = existingWorkerId != null && !this.workerId.equals(existingWorkerId);
        final boolean lockValidation = existingLockExpirationTime != null && !ClockUtil.getCurrentTime().after(existingLockExpirationTime);
        return workerValidation && lockValidation;
    }
    
    @Override
    protected void validateInput() {
        super.validateInput();
        EnsureUtil.ensurePositive(BadUserRequestException.class, "lockDuration", this.lockDuration);
    }
}
