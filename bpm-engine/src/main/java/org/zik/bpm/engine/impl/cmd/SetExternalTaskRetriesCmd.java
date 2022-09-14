// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;

public class SetExternalTaskRetriesCmd extends ExternalTaskCmd
{
    protected int retries;
    protected boolean writeUserOperationLog;
    
    public SetExternalTaskRetriesCmd(final String externalTaskId, final int retries, final boolean writeUserOperationLog) {
        super(externalTaskId);
        this.retries = retries;
        this.writeUserOperationLog = writeUserOperationLog;
    }
    
    @Override
    protected void validateInput() {
        EnsureUtil.ensureGreaterThanOrEqual(BadUserRequestException.class, "The number of retries cannot be negative", "retries", this.retries, 0L);
    }
    
    @Override
    protected void execute(final ExternalTaskEntity externalTask) {
        externalTask.setRetriesAndManageIncidents(this.retries);
    }
    
    @Override
    protected String getUserOperationLogOperationType() {
        if (this.writeUserOperationLog) {
            return "SetExternalTaskRetries";
        }
        return super.getUserOperationLogOperationType();
    }
    
    @Override
    protected List<PropertyChange> getUserOperationLogPropertyChanges(final ExternalTaskEntity externalTask) {
        if (this.writeUserOperationLog) {
            return Collections.singletonList(new PropertyChange("retries", externalTask.getRetries(), this.retries));
        }
        return super.getUserOperationLogPropertyChanges(externalTask);
    }
}
