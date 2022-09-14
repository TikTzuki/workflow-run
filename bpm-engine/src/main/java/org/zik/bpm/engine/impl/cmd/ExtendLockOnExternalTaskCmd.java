// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;

public class ExtendLockOnExternalTaskCmd extends HandleExternalTaskCmd
{
    private long newLockTime;
    
    public ExtendLockOnExternalTaskCmd(final String externalTaskId, final String workerId, final long newLockTime) {
        super(externalTaskId, workerId);
        EnsureUtil.ensurePositive(BadUserRequestException.class, "lockTime", newLockTime);
        this.newLockTime = newLockTime;
    }
    
    @Override
    public String getErrorMessageOnWrongWorkerAccess() {
        return "The lock of the External Task " + this.externalTaskId + " cannot be extended by worker '" + this.workerId + "'";
    }
    
    @Override
    protected void execute(final ExternalTaskEntity externalTask) {
        EnsureUtil.ensureGreaterThanOrEqual(BadUserRequestException.class, "Cannot extend a lock that expired", "lockExpirationTime", externalTask.getLockExpirationTime().getTime(), ClockUtil.getCurrentTime().getTime());
        externalTask.extendLock(this.newLockTime);
    }
}
