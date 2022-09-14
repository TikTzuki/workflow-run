// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.util.Map;

public class PatchTaskVariablesCmd extends AbstractPatchVariablesCmd
{
    private static final long serialVersionUID = 1L;
    
    public PatchTaskVariablesCmd(final String taskId, final Map<String, ?> modifications, final Collection<String> deletions, final boolean isLocal) {
        super(taskId, modifications, deletions, isLocal);
    }
    
    @Override
    protected AbstractSetVariableCmd getSetVariableCmd() {
        return new SetTaskVariablesCmd(this.entityId, this.variables, this.isLocal);
    }
    
    @Override
    protected AbstractRemoveVariableCmd getRemoveVariableCmd() {
        return new RemoveTaskVariablesCmd(this.entityId, this.deletions, this.isLocal);
    }
    
    public void logVariableOperation(final CommandContext commandContext) {
        commandContext.getOperationLogManager().logVariableOperation(this.getLogEntryOperation(), null, this.entityId, PropertyChange.EMPTY_CHANGE);
    }
}
