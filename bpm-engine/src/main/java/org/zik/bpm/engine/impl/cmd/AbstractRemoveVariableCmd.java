// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import java.util.Collection;

public abstract class AbstractRemoveVariableCmd extends AbstractVariableCmd
{
    private static final long serialVersionUID = 1L;
    protected final Collection<String> variableNames;
    
    public AbstractRemoveVariableCmd(final String entityId, final Collection<String> variableNames, final boolean isLocal) {
        super(entityId, isLocal);
        this.variableNames = variableNames;
    }
    
    @Override
    protected void executeOperation(final AbstractVariableScope scope) {
        if (this.isLocal) {
            scope.removeVariablesLocal(this.variableNames);
        }
        else {
            scope.removeVariables(this.variableNames);
        }
    }
    
    @Override
    protected String getLogEntryOperation() {
        return "RemoveVariable";
    }
}
