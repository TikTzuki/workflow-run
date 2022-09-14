// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class UpdateProcessDefinitionHistoryTimeToLiveCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    protected Integer historyTimeToLive;
    
    public UpdateProcessDefinitionHistoryTimeToLiveCmd(final String processDefinitionId, final Integer historyTimeToLive) {
        this.processDefinitionId = processDefinitionId;
        this.historyTimeToLive = historyTimeToLive;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.checkAuthorization(commandContext);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "processDefinitionId", (Object)this.processDefinitionId);
        if (this.historyTimeToLive != null) {
            EnsureUtil.ensureGreaterThanOrEqual(BadUserRequestException.class, "", "historyTimeToLive", this.historyTimeToLive, 0L);
        }
        final ProcessDefinitionEntity processDefinitionEntity = commandContext.getProcessDefinitionManager().findLatestProcessDefinitionById(this.processDefinitionId);
        this.logUserOperation(commandContext, processDefinitionEntity);
        processDefinitionEntity.setHistoryTimeToLive(this.historyTimeToLive);
        return null;
    }
    
    protected void checkAuthorization(final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessDefinitionById(this.processDefinitionId);
        }
    }
    
    protected void logUserOperation(final CommandContext commandContext, final ProcessDefinitionEntity processDefinitionEntity) {
        final PropertyChange propertyChange = new PropertyChange("historyTimeToLive", processDefinitionEntity.getHistoryTimeToLive(), this.historyTimeToLive);
        commandContext.getOperationLogManager().logProcessDefinitionOperation("UpdateHistoryTimeToLive", this.processDefinitionId, processDefinitionEntity.getKey(), propertyChange);
    }
}
