// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CreateTaskCmd implements Command<Task>
{
    protected String taskId;
    
    public CreateTaskCmd(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public Task execute(final CommandContext commandContext) {
        this.checkCreateTask(commandContext);
        return new TaskEntity(this.taskId);
    }
    
    protected void checkCreateTask(final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkCreateTask();
        }
    }
}
