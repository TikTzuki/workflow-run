// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Map;

public class HandleExternalTaskBpmnErrorCmd extends HandleExternalTaskCmd
{
    protected String errorCode;
    protected String errorMessage;
    protected Map<String, Object> variables;
    
    public HandleExternalTaskBpmnErrorCmd(final String externalTaskId, final String workerId, final String errorCode) {
        super(externalTaskId, workerId);
        this.errorCode = errorCode;
    }
    
    public HandleExternalTaskBpmnErrorCmd(final String externalTaskId, final String workerId, final String errorCode, final String errorMessage, final Map<String, Object> variables) {
        super(externalTaskId, workerId);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.variables = variables;
    }
    
    @Override
    protected void validateInput() {
        super.validateInput();
        EnsureUtil.ensureNotNull("errorCode", (Object)this.errorCode);
    }
    
    @Override
    public String getErrorMessageOnWrongWorkerAccess() {
        return "Bpmn error of External Task " + this.externalTaskId + " cannot be reported by worker '" + this.workerId;
    }
    
    public void execute(final ExternalTaskEntity externalTask) {
        externalTask.bpmnError(this.errorCode, this.errorMessage, this.variables);
    }
}
