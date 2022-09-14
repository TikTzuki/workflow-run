// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.GetTaskFormCmd;
import org.zik.bpm.engine.form.FormData;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.cmd.AbstractGetDeployedFormCmd;

public class GetDeployedTaskFormCmd extends AbstractGetDeployedFormCmd
{
    protected String taskId;
    
    public GetDeployedTaskFormCmd(final String taskId) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Task id cannot be null", "taskId", taskId);
        this.taskId = taskId;
    }
    
    @Override
    protected FormData getFormData() {
        return this.commandContext.runWithoutAuthorization((Command<FormData>)new GetTaskFormCmd(this.taskId));
    }
    
    @Override
    protected void checkAuthorization() {
        final TaskEntity taskEntity = this.commandContext.getTaskManager().findTaskById(this.taskId);
        for (final CommandChecker checker : this.commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadTask(taskEntity);
        }
    }
}
