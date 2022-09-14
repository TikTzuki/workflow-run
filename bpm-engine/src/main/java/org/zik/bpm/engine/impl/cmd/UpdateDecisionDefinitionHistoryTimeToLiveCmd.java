// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class UpdateDecisionDefinitionHistoryTimeToLiveCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String decisionDefinitionId;
    protected Integer historyTimeToLive;
    
    public UpdateDecisionDefinitionHistoryTimeToLiveCmd(final String decisionDefinitionId, final Integer historyTimeToLive) {
        this.decisionDefinitionId = decisionDefinitionId;
        this.historyTimeToLive = historyTimeToLive;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.checkAuthorization(commandContext);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "decisionDefinitionId", (Object)this.decisionDefinitionId);
        if (this.historyTimeToLive != null) {
            EnsureUtil.ensureGreaterThanOrEqual(BadUserRequestException.class, "", "historyTimeToLive", this.historyTimeToLive, 0L);
        }
        final DecisionDefinitionEntity decisionDefinitionEntity = commandContext.getDecisionDefinitionManager().findDecisionDefinitionById(this.decisionDefinitionId);
        this.logUserOperation(commandContext, decisionDefinitionEntity);
        decisionDefinitionEntity.setHistoryTimeToLive(this.historyTimeToLive);
        return null;
    }
    
    protected void checkAuthorization(final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateDecisionDefinitionById(this.decisionDefinitionId);
        }
    }
    
    protected void logUserOperation(final CommandContext commandContext, final DecisionDefinitionEntity decisionDefinitionEntity) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("historyTimeToLive", decisionDefinitionEntity.getHistoryTimeToLive(), this.historyTimeToLive));
        propertyChanges.add(new PropertyChange("decisionDefinitionId", null, this.decisionDefinitionId));
        propertyChanges.add(new PropertyChange("decisionDefinitionKey", null, decisionDefinitionEntity.getKey()));
        commandContext.getOperationLogManager().logDecisionDefinitionOperation("UpdateHistoryTimeToLive", propertyChanges);
    }
}
