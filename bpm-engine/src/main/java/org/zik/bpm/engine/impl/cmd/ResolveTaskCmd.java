// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import java.util.Map;

public class ResolveTaskCmd extends CompleteTaskCmd
{
    private static final long serialVersionUID = 1L;
    
    public ResolveTaskCmd(final String taskId, final Map<String, Object> variables) {
        super(taskId, variables, false, false);
    }
    
    @Override
    protected void completeTask(final TaskEntity task) {
        task.resolve();
        task.triggerUpdateEvent();
        task.logUserOperation("Resolve");
    }
}
