// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetExternalTaskErrorDetailsCmd implements Command<String>, Serializable
{
    private static final long serialVersionUID = 1L;
    private String externalTaskId;
    
    public GetExternalTaskErrorDetailsCmd(final String externalTaskId) {
        this.externalTaskId = externalTaskId;
    }
    
    @Override
    public String execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("externalTaskId", (Object)this.externalTaskId);
        final ExternalTaskEntity externalTask = commandContext.getExternalTaskManager().findExternalTaskById(this.externalTaskId);
        EnsureUtil.ensureNotNull("No external task found with id " + this.externalTaskId, "externalTask", externalTask);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessInstance(externalTask.getProcessInstanceId());
        }
        return externalTask.getErrorDetails();
    }
}
