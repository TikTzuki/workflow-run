// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.migration;

import org.zik.bpm.engine.BadUserRequestException;

public class MigrationPlanValidationException extends BadUserRequestException
{
    private static final long serialVersionUID = 1L;
    protected MigrationPlanValidationReport validationReport;
    
    public MigrationPlanValidationException(final String message, final MigrationPlanValidationReport validationReport) {
        super(message);
        this.validationReport = validationReport;
    }
    
    public MigrationPlanValidationReport getValidationReport() {
        return this.validationReport;
    }
}
