// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import java.util.Map;

public class HandleExternalTaskFailureCmd extends HandleExternalTaskCmd
{
    protected String errorMessage;
    protected String errorDetails;
    protected long retryDuration;
    protected int retries;
    protected Map<String, Object> variables;
    protected Map<String, Object> localVariables;
    
    public HandleExternalTaskFailureCmd(final String externalTaskId, final String workerId, final String errorMessage, final String errorDetails, final int retries, final long retryDuration, final Map<String, Object> variables, final Map<String, Object> localVariables) {
        super(externalTaskId, workerId);
        this.errorMessage = errorMessage;
        this.errorDetails = errorDetails;
        this.retries = retries;
        this.retryDuration = retryDuration;
        this.variables = variables;
        this.localVariables = localVariables;
    }
    
    public void execute(final ExternalTaskEntity externalTask) {
        externalTask.failed(this.errorMessage, this.errorDetails, this.retries, this.retryDuration, this.variables, this.localVariables);
    }
    
    @Override
    protected void validateInput() {
        super.validateInput();
        EnsureUtil.ensureGreaterThanOrEqual("retries", this.retries, 0L);
        EnsureUtil.ensureGreaterThanOrEqual("retryDuration", this.retryDuration, 0L);
    }
    
    @Override
    public String getErrorMessageOnWrongWorkerAccess() {
        return "Failure of External Task " + this.externalTaskId + " cannot be reported by worker '" + this.workerId;
    }
}
