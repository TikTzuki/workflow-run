// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import org.zik.bpm.engine.impl.jobexecutor.AsyncContinuationJobHandler;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;

public class AsyncProcessStartMigrationValidator implements MigratingTransitionInstanceValidator
{
    @Override
    public void validate(final MigratingTransitionInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingTransitionInstanceValidationReportImpl instanceReport) {
        final ActivityImpl targetActivity = (ActivityImpl)migratingInstance.getTargetScope();
        if (targetActivity != null && this.isProcessStartJob(migratingInstance.getJobInstance().getJobEntity()) && !this.isTopLevelActivity(targetActivity)) {
            instanceReport.addFailure("A transition instance that instantiates the process can only be migrated to a process-level flow node");
        }
    }
    
    protected boolean isProcessStartJob(final JobEntity job) {
        final AsyncContinuationJobHandler.AsyncContinuationConfiguration configuration = (AsyncContinuationJobHandler.AsyncContinuationConfiguration)job.getJobHandlerConfiguration();
        return PvmAtomicOperation.PROCESS_START.getCanonicalName().equals(configuration.getAtomicOperation());
    }
    
    protected boolean isTopLevelActivity(final ActivityImpl activity) {
        return activity.getFlowScope() == activity.getProcessDefinition();
    }
}
