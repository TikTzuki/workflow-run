// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import org.zik.bpm.engine.impl.HistoricDecisionInstanceQueryImpl;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.Iterator;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricDecisionInstanceByDefinitionIdCmd implements Command<Object>
{
    protected final String decisionDefinitionId;
    
    public DeleteHistoricDecisionInstanceByDefinitionIdCmd(final String decisionDefinitionId) {
        this.decisionDefinitionId = decisionDefinitionId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("decisionDefinitionId", (Object)this.decisionDefinitionId);
        final DecisionDefinitionEntity decisionDefinition = commandContext.getDecisionDefinitionManager().findDecisionDefinitionById(this.decisionDefinitionId);
        EnsureUtil.ensureNotNull("No decision definition found with id: " + this.decisionDefinitionId, "decisionDefinition", decisionDefinition);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteHistoricDecisionInstance(decisionDefinition.getKey());
        }
        final long numInstances = this.getDecisionInstanceCount(commandContext);
        this.writeUserOperationLog(commandContext, numInstances);
        commandContext.getHistoricDecisionInstanceManager().deleteHistoricDecisionInstancesByDecisionDefinitionId(this.decisionDefinitionId);
        return null;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final long numInstances) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, false));
        commandContext.getOperationLogManager().logDecisionInstanceOperation("DeleteHistory", propertyChanges);
    }
    
    protected long getDecisionInstanceCount(final CommandContext commandContext) {
        final HistoricDecisionInstanceQueryImpl historicDecisionInstanceQuery = new HistoricDecisionInstanceQueryImpl();
        historicDecisionInstanceQuery.decisionDefinitionId(this.decisionDefinitionId);
        return commandContext.getHistoricDecisionInstanceManager().findHistoricDecisionInstanceCountByQueryCriteria(historicDecisionInstanceQuery);
    }
}
