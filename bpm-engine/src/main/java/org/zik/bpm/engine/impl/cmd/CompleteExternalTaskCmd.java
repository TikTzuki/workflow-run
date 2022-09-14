// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import java.util.Map;

public class CompleteExternalTaskCmd extends HandleExternalTaskCmd
{
    protected Map<String, Object> variables;
    protected Map<String, Object> localVariables;
    
    public CompleteExternalTaskCmd(final String externalTaskId, final String workerId, final Map<String, Object> variables, final Map<String, Object> localVariables) {
        super(externalTaskId, workerId);
        this.localVariables = localVariables;
        this.variables = variables;
    }
    
    @Override
    public String getErrorMessageOnWrongWorkerAccess() {
        return "External Task " + this.externalTaskId + " cannot be completed by worker '" + this.workerId;
    }
    
    public void execute(final ExternalTaskEntity externalTask) {
        externalTask.complete(this.variables, this.localVariables);
    }
}
