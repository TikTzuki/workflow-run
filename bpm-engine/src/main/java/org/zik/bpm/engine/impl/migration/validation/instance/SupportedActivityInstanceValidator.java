// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.migration.validation.activity.SupportedActivityValidator;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;

public class SupportedActivityInstanceValidator implements MigratingActivityInstanceValidator
{
    @Override
    public void validate(final MigratingActivityInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingActivityInstanceValidationReportImpl instanceReport) {
        final ScopeImpl sourceScope = migratingInstance.getSourceScope();
        if (sourceScope != sourceScope.getProcessDefinition()) {
            final ActivityImpl sourceActivity = (ActivityImpl)migratingInstance.getSourceScope();
            if (!SupportedActivityValidator.INSTANCE.isSupportedActivity(sourceActivity)) {
                instanceReport.addFailure("The type of the source activity is not supported for activity instance migration");
            }
        }
    }
}
