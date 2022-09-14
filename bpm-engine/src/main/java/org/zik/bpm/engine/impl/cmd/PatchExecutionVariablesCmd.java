// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.util.Map;

public class PatchExecutionVariablesCmd extends AbstractPatchVariablesCmd
{
    private static final long serialVersionUID = 1L;
    
    public PatchExecutionVariablesCmd(final String executionId, final Map<String, ?> modifications, final Collection<String> deletions, final boolean isLocal) {
        super(executionId, modifications, deletions, isLocal);
    }
    
    @Override
    protected SetExecutionVariablesCmd getSetVariableCmd() {
        return new SetExecutionVariablesCmd(this.entityId, this.variables, this.isLocal);
    }
    
    @Override
    protected RemoveExecutionVariablesCmd getRemoveVariableCmd() {
        return new RemoveExecutionVariablesCmd(this.entityId, this.deletions, this.isLocal);
    }
    
    public void logVariableOperation(final CommandContext commandContext) {
        commandContext.getOperationLogManager().logVariableOperation(this.getLogEntryOperation(), this.entityId, null, PropertyChange.EMPTY_CHANGE);
    }
}
