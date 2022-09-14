// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class UpdateCaseDefinitionHistoryTimeToLiveCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String caseDefinitionId;
    protected Integer historyTimeToLive;
    
    public UpdateCaseDefinitionHistoryTimeToLiveCmd(final String caseDefinitionId, final Integer historyTimeToLive) {
        this.caseDefinitionId = caseDefinitionId;
        this.historyTimeToLive = historyTimeToLive;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "caseDefinitionId", (Object)this.caseDefinitionId);
        if (this.historyTimeToLive != null) {
            EnsureUtil.ensureGreaterThanOrEqual(BadUserRequestException.class, "", "historyTimeToLive", this.historyTimeToLive, 0L);
        }
        final CaseDefinitionEntity caseDefinitionEntity = commandContext.getCaseDefinitionManager().findLatestDefinitionById(this.caseDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateCaseDefinition(caseDefinitionEntity);
        }
        this.logUserOperation(commandContext, caseDefinitionEntity);
        caseDefinitionEntity.setHistoryTimeToLive(this.historyTimeToLive);
        return null;
    }
    
    protected void logUserOperation(final CommandContext commandContext, final CaseDefinitionEntity caseDefinitionEntity) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("historyTimeToLive", caseDefinitionEntity.getHistoryTimeToLive(), this.historyTimeToLive));
        propertyChanges.add(new PropertyChange("caseDefinitionKey", null, caseDefinitionEntity.getKey()));
        commandContext.getOperationLogManager().logCaseDefinitionOperation("UpdateHistoryTimeToLive", this.caseDefinitionId, propertyChanges);
    }
}
