// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class CmmnOperationLogger extends ProcessEngineLogger
{
    public void completingSubCaseError(final CmmnExecution execution, final Throwable cause) {
        this.logError("001", "Error while completing sub case of case execution '{}'. Reason: '{}'", new Object[] { execution, cause.getMessage(), cause });
    }
    
    public ProcessEngineException completingSubCaseErrorException(final CmmnExecution execution, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("002", "Error while completing sub case of case execution '{}'.", new Object[] { execution }), cause);
    }
    
    public BadUserRequestException exceptionCreateCaseInstanceByIdAndTenantId() {
        return new BadUserRequestException(this.exceptionMessage("003", "Cannot specify a tenant-id when create a case instance by case definition id.", new Object[0]));
    }
}
