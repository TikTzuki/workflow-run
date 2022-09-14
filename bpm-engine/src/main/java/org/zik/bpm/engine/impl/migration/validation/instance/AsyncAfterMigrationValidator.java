// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingJobInstance;
import org.zik.bpm.engine.impl.jobexecutor.AsyncContinuationJobHandler;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;

public class AsyncAfterMigrationValidator implements MigratingTransitionInstanceValidator
{
    @Override
    public void validate(final MigratingTransitionInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingTransitionInstanceValidationReportImpl instanceReport) {
        final ActivityImpl targetActivity = (ActivityImpl)migratingInstance.getTargetScope();
        if (targetActivity != null && migratingInstance.isAsyncAfter()) {
            final MigratingJobInstance jobInstance = migratingInstance.getJobInstance();
            final AsyncContinuationJobHandler.AsyncContinuationConfiguration config = (AsyncContinuationJobHandler.AsyncContinuationConfiguration)jobInstance.getJobEntity().getJobHandlerConfiguration();
            final String sourceTransitionId = config.getTransitionId();
            if (targetActivity.getOutgoingTransitions().size() > 1) {
                if (sourceTransitionId == null) {
                    instanceReport.addFailure("Transition instance is assigned to no sequence flow and target activity has more than one outgoing sequence flow");
                }
                else {
                    final TransitionImpl matchingOutgoingTransition = targetActivity.findOutgoingTransition(sourceTransitionId);
                    if (matchingOutgoingTransition == null) {
                        instanceReport.addFailure("Transition instance is assigned to a sequence flow that cannot be matched in the target activity");
                    }
                }
            }
        }
    }
}
