// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;

public class AsyncMigrationValidator implements MigratingTransitionInstanceValidator
{
    @Override
    public void validate(final MigratingTransitionInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingTransitionInstanceValidationReportImpl instanceReport) {
        final ActivityImpl targetActivity = (ActivityImpl)migratingInstance.getTargetScope();
        if (targetActivity != null) {
            if (migratingInstance.isAsyncAfter()) {
                if (!targetActivity.isAsyncAfter()) {
                    instanceReport.addFailure("Target activity is not asyncAfter");
                }
            }
            else if (!targetActivity.isAsyncBefore()) {
                instanceReport.addFailure("Target activity is not asyncBefore");
            }
        }
    }
}
