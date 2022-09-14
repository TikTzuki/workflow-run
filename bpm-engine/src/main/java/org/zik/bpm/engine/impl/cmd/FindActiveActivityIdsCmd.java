// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionManager;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class FindActiveActivityIdsCmd implements Command<List<String>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String executionId;
    
    public FindActiveActivityIdsCmd(final String executionId) {
        this.executionId = executionId;
    }
    
    @Override
    public List<String> execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("executionId", (Object)this.executionId);
        final ExecutionManager executionManager = commandContext.getExecutionManager();
        final ExecutionEntity execution = executionManager.findExecutionById(this.executionId);
        EnsureUtil.ensureNotNull("execution " + this.executionId + " doesn't exist", "execution", execution);
        this.checkGetActivityIds(execution, commandContext);
        return execution.findActiveActivityIds();
    }
    
    protected void checkGetActivityIds(final ExecutionEntity execution, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessInstance(execution);
        }
    }
}
