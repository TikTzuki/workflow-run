// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.migration;

import org.zik.bpm.engine.ProcessEngineException;

public class MigratingProcessInstanceValidationException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    protected MigratingProcessInstanceValidationReport validationReport;
    
    public MigratingProcessInstanceValidationException(final String message, final MigratingProcessInstanceValidationReport validationReport) {
        super(message);
        this.validationReport = validationReport;
    }
    
    public MigratingProcessInstanceValidationReport getValidationReport() {
        return this.validationReport;
    }
}
