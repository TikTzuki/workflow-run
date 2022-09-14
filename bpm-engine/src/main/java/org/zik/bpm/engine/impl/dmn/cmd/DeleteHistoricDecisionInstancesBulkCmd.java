// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricDecisionInstancesBulkCmd implements Command<Object>
{
    protected final List<String> decisionInstanceIds;
    
    public DeleteHistoricDecisionInstancesBulkCmd(final List<String> decisionInstanceIds) {
        this.decisionInstanceIds = decisionInstanceIds;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkAuthorization(Permissions.DELETE_HISTORY, Resources.DECISION_DEFINITION);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "decisionInstanceIds", this.decisionInstanceIds);
        this.writeUserOperationLog(commandContext, this.decisionInstanceIds.size());
        commandContext.getHistoricDecisionInstanceManager().deleteHistoricDecisionInstanceByIds(this.decisionInstanceIds);
        return null;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, false));
        commandContext.getOperationLogManager().logDecisionInstanceOperation("DeleteHistory", propertyChanges);
    }
}
