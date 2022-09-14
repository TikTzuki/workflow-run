// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public abstract class HandleExternalTaskCmd extends ExternalTaskCmd
{
    protected String workerId;
    
    public HandleExternalTaskCmd(final String externalTaskId, final String workerId) {
        super(externalTaskId);
        this.workerId = workerId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.validateInput();
        final ExternalTaskEntity externalTask = commandContext.getExternalTaskManager().findExternalTaskById(this.externalTaskId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "Cannot find external task with id " + this.externalTaskId, "externalTask", externalTask);
        if (this.validateWorkerViolation(externalTask)) {
            throw new BadUserRequestException(this.getErrorMessageOnWrongWorkerAccess() + "'. It is locked by worker '" + externalTask.getWorkerId() + "'.");
        }
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessInstanceById(externalTask.getProcessInstanceId());
        }
        this.execute(externalTask);
        return null;
    }
    
    public abstract String getErrorMessageOnWrongWorkerAccess();
    
    @Override
    protected void validateInput() {
        EnsureUtil.ensureNotNull("workerId", (Object)this.workerId);
    }
    
    protected boolean validateWorkerViolation(final ExternalTaskEntity externalTask) {
        return !this.workerId.equals(externalTask.getWorkerId());
    }
}
