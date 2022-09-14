// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Map;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SignalCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String executionId;
    protected String signalName;
    protected Object signalData;
    protected final Map<String, Object> processVariables;
    
    public SignalCmd(final String executionId, final String signalName, final Object signalData, final Map<String, Object> processVariables) {
        this.executionId = executionId;
        this.signalName = signalName;
        this.signalData = signalData;
        this.processVariables = processVariables;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "executionId is null", "executionId", this.executionId);
        final ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(this.executionId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "execution " + this.executionId + " doesn't exist", "execution", execution);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessInstance(execution);
        }
        if (this.processVariables != null) {
            execution.setVariables(this.processVariables);
        }
        execution.signal(this.signalName, this.signalData);
        return null;
    }
}
