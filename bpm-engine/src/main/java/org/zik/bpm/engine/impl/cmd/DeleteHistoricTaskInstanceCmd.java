// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.List;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricTaskInstanceCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    
    public DeleteHistoricTaskInstanceCmd(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        final HistoricTaskInstanceEntity task = commandContext.getHistoricTaskInstanceManager().findHistoricTaskInstanceById(this.taskId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteHistoricTaskInstance(task);
        }
        this.writeUserOperationLog(commandContext, task);
        commandContext.getHistoricTaskInstanceManager().deleteHistoricTaskInstanceById(this.taskId);
        return null;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final HistoricTaskInstanceEntity historicTask) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, 1));
        propertyChanges.add(new PropertyChange("async", null, false));
        commandContext.getOperationLogManager().logTaskOperations("DeleteHistory", historicTask, propertyChanges);
    }
}
