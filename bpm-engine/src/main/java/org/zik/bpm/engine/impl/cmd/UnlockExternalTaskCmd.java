// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;

public class UnlockExternalTaskCmd extends ExternalTaskCmd
{
    public UnlockExternalTaskCmd(final String externalTaskId) {
        super(externalTaskId);
    }
    
    @Override
    protected void validateInput() {
    }
    
    @Override
    protected void execute(final ExternalTaskEntity externalTask) {
        externalTask.unlock();
    }
    
    @Override
    protected String getUserOperationLogOperationType() {
        return "Unlock";
    }
}
