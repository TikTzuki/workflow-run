// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.util.Map;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractPatchVariablesCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String entityId;
    protected Map<String, ?> variables;
    protected Collection<String> deletions;
    protected boolean isLocal;
    
    public AbstractPatchVariablesCmd(final String entityId, final Map<String, ?> variables, final Collection<String> deletions, final boolean isLocal) {
        this.entityId = entityId;
        this.variables = variables;
        this.deletions = deletions;
        this.isLocal = isLocal;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.getSetVariableCmd().disableLogUserOperation().execute(commandContext);
        this.getRemoveVariableCmd().disableLogUserOperation().execute(commandContext);
        this.logVariableOperation(commandContext);
        return null;
    }
    
    protected String getLogEntryOperation() {
        return "ModifyVariable";
    }
    
    protected abstract AbstractSetVariableCmd getSetVariableCmd();
    
    protected abstract AbstractRemoveVariableCmd getRemoveVariableCmd();
    
    protected abstract void logVariableOperation(final CommandContext p0);
}
