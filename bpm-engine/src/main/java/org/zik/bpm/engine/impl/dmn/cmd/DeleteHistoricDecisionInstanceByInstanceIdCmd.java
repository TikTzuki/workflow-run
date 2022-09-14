// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.Iterator;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import java.util.Arrays;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricDecisionInstanceByInstanceIdCmd implements Command<Object>
{
    protected final String historicDecisionInstanceId;
    
    public DeleteHistoricDecisionInstanceByInstanceIdCmd(final String historicDecisionInstanceId) {
        this.historicDecisionInstanceId = historicDecisionInstanceId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("historicDecisionInstanceId", (Object)this.historicDecisionInstanceId);
        final HistoricDecisionInstance historicDecisionInstance = commandContext.getHistoricDecisionInstanceManager().findHistoricDecisionInstance(this.historicDecisionInstanceId);
        EnsureUtil.ensureNotNull("No historic decision instance found with id: " + this.historicDecisionInstanceId, "historicDecisionInstance", historicDecisionInstance);
        this.writeUserOperationLog(commandContext);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteHistoricDecisionInstance(historicDecisionInstance);
        }
        commandContext.getHistoricDecisionInstanceManager().deleteHistoricDecisionInstanceByIds(Arrays.asList(this.historicDecisionInstanceId));
        return null;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, 1));
        propertyChanges.add(new PropertyChange("async", null, false));
        commandContext.getOperationLogManager().logDecisionInstanceOperation("DeleteHistory", propertyChanges);
    }
}
