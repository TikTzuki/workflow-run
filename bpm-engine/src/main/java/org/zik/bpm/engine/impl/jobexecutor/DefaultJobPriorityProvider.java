// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.DefaultPriorityProvider;

public class DefaultJobPriorityProvider extends DefaultPriorityProvider<JobDeclaration<?, ?>>
{
    private static final JobExecutorLogger LOG;
    
    @Override
    protected Long getSpecificPriority(final ExecutionEntity execution, final JobDeclaration<?, ?> param, final String jobDefinitionId) {
        Long specificPriority = null;
        final JobDefinitionEntity jobDefinition = this.getJobDefinitionFor(jobDefinitionId);
        if (jobDefinition != null) {
            specificPriority = jobDefinition.getOverridingJobPriority();
        }
        if (specificPriority == null) {
            final ParameterValueProvider priorityProvider = param.getJobPriorityProvider();
            if (priorityProvider != null) {
                specificPriority = this.evaluateValueProvider(priorityProvider, execution, this.describeContext(param, execution));
            }
        }
        return specificPriority;
    }
    
    @Override
    protected Long getProcessDefinitionPriority(final ExecutionEntity execution, final JobDeclaration<?, ?> jobDeclaration) {
        final ProcessDefinitionImpl processDefinition = jobDeclaration.getProcessDefinition();
        return this.getProcessDefinedPriority(processDefinition, "jobPriority", execution, this.describeContext(jobDeclaration, execution));
    }
    
    protected JobDefinitionEntity getJobDefinitionFor(final String jobDefinitionId) {
        if (jobDefinitionId != null) {
            return Context.getCommandContext().getJobDefinitionManager().findById(jobDefinitionId);
        }
        return null;
    }
    
    protected Long getActivityPriority(final ExecutionEntity execution, final JobDeclaration<?, ?> jobDeclaration) {
        if (jobDeclaration != null) {
            final ParameterValueProvider priorityProvider = jobDeclaration.getJobPriorityProvider();
            if (priorityProvider != null) {
                return this.evaluateValueProvider(priorityProvider, execution, this.describeContext(jobDeclaration, execution));
            }
        }
        return null;
    }
    
    @Override
    protected void logNotDeterminingPriority(final ExecutionEntity execution, final Object value, final ProcessEngineException e) {
        DefaultJobPriorityProvider.LOG.couldNotDeterminePriority(execution, value, e);
    }
    
    protected String describeContext(final JobDeclaration<?, ?> jobDeclaration, final ExecutionEntity executionEntity) {
        return "Job " + jobDeclaration.getActivityId() + "/" + jobDeclaration.getJobHandlerType() + " instantiated in context of " + executionEntity;
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
