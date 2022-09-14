// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.externaltask;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.bpmn.behavior.ExternalTaskActivityBehavior;
import org.zik.bpm.engine.impl.DefaultPriorityProvider;

public class DefaultExternalTaskPriorityProvider extends DefaultPriorityProvider<ExternalTaskActivityBehavior>
{
    public static final ExternalTaskLogger LOG;
    
    @Override
    protected void logNotDeterminingPriority(final ExecutionEntity execution, final Object value, final ProcessEngineException e) {
        DefaultExternalTaskPriorityProvider.LOG.couldNotDeterminePriority(execution, value, e);
    }
    
    public Long getSpecificPriority(final ExecutionEntity execution, final ExternalTaskActivityBehavior param, final String jobDefinitionId) {
        final ParameterValueProvider priorityProvider = param.getPriorityValueProvider();
        if (priorityProvider != null) {
            return this.evaluateValueProvider(priorityProvider, execution, "");
        }
        return null;
    }
    
    @Override
    protected Long getProcessDefinitionPriority(final ExecutionEntity execution, final ExternalTaskActivityBehavior param) {
        return this.getProcessDefinedPriority(execution.getProcessDefinition(), "taskPriority", execution, "");
    }
    
    static {
        LOG = ProcessEngineLogger.EXTERNAL_TASK_LOGGER;
    }
}
