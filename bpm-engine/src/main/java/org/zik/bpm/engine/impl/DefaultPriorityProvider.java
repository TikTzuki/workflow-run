// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;

public abstract class DefaultPriorityProvider<T> implements PriorityProvider<T>
{
    public static long DEFAULT_PRIORITY;
    public static long DEFAULT_PRIORITY_ON_RESOLUTION_FAILURE;
    
    public long getDefaultPriority() {
        return DefaultPriorityProvider.DEFAULT_PRIORITY;
    }
    
    public long getDefaultPriorityOnResolutionFailure() {
        return DefaultPriorityProvider.DEFAULT_PRIORITY_ON_RESOLUTION_FAILURE;
    }
    
    protected Long evaluateValueProvider(final ParameterValueProvider valueProvider, final ExecutionEntity execution, final String errorMessageHeading) {
        Object value;
        try {
            value = valueProvider.getValue(execution);
        }
        catch (ProcessEngineException e) {
            if (!Context.getProcessEngineConfiguration().isEnableGracefulDegradationOnContextSwitchFailure() || !this.isSymptomOfContextSwitchFailure(e, execution)) {
                throw e;
            }
            value = this.getDefaultPriorityOnResolutionFailure();
            this.logNotDeterminingPriority(execution, value, e);
        }
        if (!(value instanceof Number)) {
            throw new ProcessEngineException(errorMessageHeading + ": Priority value is not an Integer");
        }
        final Number numberValue = (Number)value;
        if (this.isValidLongValue(numberValue)) {
            return numberValue.longValue();
        }
        throw new ProcessEngineException(errorMessageHeading + ": Priority value must be either Short, Integer, or Long");
    }
    
    @Override
    public long determinePriority(final ExecutionEntity execution, final T param, final String jobDefinitionId) {
        if (param != null || execution != null) {
            final Long specificPriority = this.getSpecificPriority(execution, param, jobDefinitionId);
            if (specificPriority != null) {
                return specificPriority;
            }
            final Long processDefinitionPriority = this.getProcessDefinitionPriority(execution, param);
            if (processDefinitionPriority != null) {
                return processDefinitionPriority;
            }
        }
        return this.getDefaultPriority();
    }
    
    protected abstract Long getSpecificPriority(final ExecutionEntity p0, final T p1, final String p2);
    
    protected abstract Long getProcessDefinitionPriority(final ExecutionEntity p0, final T p1);
    
    protected Long getProcessDefinedPriority(final ProcessDefinitionImpl processDefinition, final String propertyKey, final ExecutionEntity execution, final String errorMsgHead) {
        if (processDefinition != null) {
            final ParameterValueProvider priorityProvider = (ParameterValueProvider)processDefinition.getProperty(propertyKey);
            if (priorityProvider != null) {
                return this.evaluateValueProvider(priorityProvider, execution, errorMsgHead);
            }
        }
        return null;
    }
    
    protected abstract void logNotDeterminingPriority(final ExecutionEntity p0, final Object p1, final ProcessEngineException p2);
    
    protected boolean isSymptomOfContextSwitchFailure(final Throwable t, final ExecutionEntity contextExecution) {
        return ProcessApplicationContextUtil.getTargetProcessApplication(contextExecution) == null;
    }
    
    protected boolean isValidLongValue(final Number value) {
        return value instanceof Short || value instanceof Integer || value instanceof Long;
    }
    
    static {
        DefaultPriorityProvider.DEFAULT_PRIORITY = 0L;
        DefaultPriorityProvider.DEFAULT_PRIORITY_ON_RESOLUTION_FAILURE = 0L;
    }
}
