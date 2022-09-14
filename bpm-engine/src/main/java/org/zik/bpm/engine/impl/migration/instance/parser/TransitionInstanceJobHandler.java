// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.migration.instance.MigratingJobInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingAsyncJobInstance;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.List;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;

public class TransitionInstanceJobHandler implements MigratingDependentInstanceParseHandler<MigratingTransitionInstance, List<JobEntity>>
{
    @Override
    public void handle(final MigratingInstanceParseContext parseContext, final MigratingTransitionInstance transitionInstance, final List<JobEntity> elements) {
        for (final JobEntity job : elements) {
            if (!isAsyncContinuation(job)) {
                continue;
            }
            final ScopeImpl targetScope = transitionInstance.getTargetScope();
            if (targetScope != null) {
                final JobDefinitionEntity targetJobDefinitionEntity = parseContext.getTargetJobDefinition(transitionInstance.getTargetScope().getId(), job.getJobHandlerType());
                final MigratingAsyncJobInstance migratingJobInstance = new MigratingAsyncJobInstance(job, targetJobDefinitionEntity, transitionInstance.getTargetScope());
                transitionInstance.setDependentJobInstance(migratingJobInstance);
                parseContext.submit(migratingJobInstance);
            }
            parseContext.consume(job);
        }
    }
    
    protected static boolean isAsyncContinuation(final JobEntity job) {
        return job != null && "async-continuation".equals(job.getJobHandlerType());
    }
}
