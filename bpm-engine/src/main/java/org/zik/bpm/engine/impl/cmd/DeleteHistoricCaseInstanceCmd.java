// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.history.HistoricCaseInstance;
import java.util.Arrays;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricCaseInstanceCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String caseInstanceId;
    
    public DeleteHistoricCaseInstanceCmd(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("caseInstanceId", (Object)this.caseInstanceId);
        final HistoricCaseInstance instance = commandContext.getHistoricCaseInstanceManager().findHistoricCaseInstance(this.caseInstanceId);
        EnsureUtil.ensureNotNull("No historic case instance found with id: " + this.caseInstanceId, "instance", instance);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteHistoricCaseInstance(instance);
        }
        EnsureUtil.ensureNotNull("Case instance is still running, cannot delete historic case instance: " + this.caseInstanceId, "instance.getCloseTime()", instance.getCloseTime());
        commandContext.getOperationLogManager().logCaseInstanceOperation("DeleteHistory", this.caseInstanceId, Collections.singletonList(PropertyChange.EMPTY_CHANGE));
        commandContext.getHistoricCaseInstanceManager().deleteHistoricCaseInstancesByIds(Arrays.asList(this.caseInstanceId));
        return null;
    }
}
