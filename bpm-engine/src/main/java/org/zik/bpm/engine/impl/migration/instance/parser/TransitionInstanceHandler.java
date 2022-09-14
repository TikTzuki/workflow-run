// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.List;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessElementInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingScopeInstance;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.runtime.TransitionInstance;

public class TransitionInstanceHandler implements MigratingInstanceParseHandler<TransitionInstance>
{
    @Override
    public void handle(final MigratingInstanceParseContext parseContext, final TransitionInstance transitionInstance) {
        if (!this.isAsyncTransitionInstance(transitionInstance)) {
            return;
        }
        final MigrationInstruction applyingInstruction = parseContext.getInstructionFor(transitionInstance.getActivityId());
        final ScopeImpl sourceScope = parseContext.getSourceProcessDefinition().findActivity(transitionInstance.getActivityId());
        ScopeImpl targetScope = null;
        if (applyingInstruction != null) {
            final String activityId = applyingInstruction.getTargetActivityId();
            targetScope = parseContext.getTargetProcessDefinition().findActivity(activityId);
        }
        final ExecutionEntity asyncExecution = Context.getCommandContext().getExecutionManager().findExecutionById(transitionInstance.getExecutionId());
        final MigratingTransitionInstance migratingTransitionInstance = parseContext.getMigratingProcessInstance().addTransitionInstance(applyingInstruction, transitionInstance, sourceScope, targetScope, asyncExecution);
        final MigratingActivityInstance parentInstance = parseContext.getMigratingActivityInstanceById(transitionInstance.getParentActivityInstanceId());
        migratingTransitionInstance.setParent(parentInstance);
        final List<JobEntity> jobs = asyncExecution.getJobs();
        parseContext.handleDependentTransitionInstanceJobs(migratingTransitionInstance, jobs);
        parseContext.handleDependentVariables(migratingTransitionInstance, this.collectTransitionInstanceVariables(migratingTransitionInstance));
    }
    
    protected boolean isAsyncTransitionInstance(final TransitionInstance transitionInstance) {
        final String executionId = transitionInstance.getExecutionId();
        final ExecutionEntity execution = Context.getCommandContext().getExecutionManager().findExecutionById(executionId);
        for (final JobEntity job : execution.getJobs()) {
            if ("async-continuation".equals(job.getJobHandlerType())) {
                return true;
            }
        }
        return false;
    }
    
    protected List<VariableInstanceEntity> collectTransitionInstanceVariables(final MigratingTransitionInstance instance) {
        final List<VariableInstanceEntity> variables = new ArrayList<VariableInstanceEntity>();
        final ExecutionEntity representativeExecution = instance.resolveRepresentativeExecution();
        if (representativeExecution.isConcurrent()) {
            variables.addAll(representativeExecution.getVariablesInternal());
        }
        else {
            variables.addAll(ActivityInstanceHandler.getConcurrentLocalVariables(representativeExecution));
        }
        return variables;
    }
}
