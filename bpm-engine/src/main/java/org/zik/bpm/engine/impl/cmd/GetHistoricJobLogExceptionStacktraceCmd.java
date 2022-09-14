// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.HistoricJobLogEventEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetHistoricJobLogExceptionStacktraceCmd implements Command<String>
{
    protected String historicJobLogId;
    
    public GetHistoricJobLogExceptionStacktraceCmd(final String historicJobLogId) {
        this.historicJobLogId = historicJobLogId;
    }
    
    @Override
    public String execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("historicJobLogId", (Object)this.historicJobLogId);
        final HistoricJobLogEventEntity job = commandContext.getHistoricJobLogManager().findHistoricJobLogById(this.historicJobLogId);
        EnsureUtil.ensureNotNull("No historic job log found with id " + this.historicJobLogId, "historicJobLog", job);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadHistoricJobLog(job);
        }
        return job.getExceptionStacktrace();
    }
}
