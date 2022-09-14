// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.task.TaskCountByCandidateGroupResult;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.db.TenantCheck;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.task.TaskReport;
import java.io.Serializable;

public class TaskReportImpl implements Serializable, TaskReport
{
    private static final long serialVersionUID = 1L;
    protected transient CommandExecutor commandExecutor;
    protected TenantCheck tenantCheck;
    
    public TaskReportImpl(final CommandExecutor commandExecutor) {
        this.tenantCheck = new TenantCheck();
        this.commandExecutor = commandExecutor;
    }
    
    protected List<TaskCountByCandidateGroupResult> createTaskCountByCandidateGroupReport(final CommandContext commandContext) {
        return commandContext.getTaskReportManager().createTaskCountByCandidateGroupReport(this);
    }
    
    public TenantCheck getTenantCheck() {
        return this.tenantCheck;
    }
    
    @Override
    public List<TaskCountByCandidateGroupResult> taskCountByCandidateGroup() {
        return this.commandExecutor.execute((Command<List<TaskCountByCandidateGroupResult>>)new TaskCountByCandidateGroupCmd());
    }
    
    protected class TaskCountByCandidateGroupCmd implements Command<List<TaskCountByCandidateGroupResult>>
    {
        @Override
        public List<TaskCountByCandidateGroupResult> execute(final CommandContext commandContext) {
            return TaskReportImpl.this.createTaskCountByCandidateGroupReport(commandContext);
        }
    }
}
