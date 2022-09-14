// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import java.util.Map;

public abstract class AbstractSetVariableCmd extends AbstractVariableCmd
{
    private static final long serialVersionUID = 1L;
    protected Map<String, ?> variables;
    protected boolean skipJavaSerializationFormatCheck;
    
    public AbstractSetVariableCmd(final String entityId, final Map<String, ?> variables, final boolean isLocal) {
        super(entityId, isLocal);
        this.variables = variables;
    }
    
    public AbstractSetVariableCmd(final String entityId, final Map<String, ?> variables, final boolean isLocal, final boolean skipJavaSerializationFormatCheck) {
        this(entityId, variables, isLocal);
        this.skipJavaSerializationFormatCheck = skipJavaSerializationFormatCheck;
    }
    
    @Override
    protected void executeOperation(final AbstractVariableScope scope) {
        if (this.isLocal) {
            scope.setVariablesLocal(this.variables, this.skipJavaSerializationFormatCheck);
        }
        else {
            scope.setVariables(this.variables, this.skipJavaSerializationFormatCheck);
        }
    }
    
    @Override
    protected String getLogEntryOperation() {
        return "SetVariable";
    }
}
