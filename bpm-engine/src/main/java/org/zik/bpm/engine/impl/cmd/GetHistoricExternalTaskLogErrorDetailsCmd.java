// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.history.event.HistoricExternalTaskLogEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetHistoricExternalTaskLogErrorDetailsCmd implements Command<String>
{
    protected String historicExternalTaskLogId;
    
    public GetHistoricExternalTaskLogErrorDetailsCmd(final String historicExternalTaskLogId) {
        this.historicExternalTaskLogId = historicExternalTaskLogId;
    }
    
    @Override
    public String execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("historicExternalTaskLogId", (Object)this.historicExternalTaskLogId);
        final HistoricExternalTaskLogEntity event = commandContext.getHistoricExternalTaskLogManager().findHistoricExternalTaskLogById(this.historicExternalTaskLogId);
        EnsureUtil.ensureNotNull("No historic external task log found with id " + this.historicExternalTaskLogId, "historicExternalTaskLog", event);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadHistoricExternalTaskLog(event);
        }
        return event.getErrorDetails();
    }
}
