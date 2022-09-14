// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import java.util.List;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.bpmn.behavior.ExternalTaskActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ELResolver;

public class VariableScopeElResolver extends ELResolver
{
    public static final String EXECUTION_KEY = "execution";
    public static final String CASE_EXECUTION_KEY = "caseExecution";
    public static final String TASK_KEY = "task";
    public static final String EXTERNAL_TASK_KEY = "externalTask";
    public static final String LOGGED_IN_USER_KEY = "authenticatedUserId";
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        final Object object = context.getContext(VariableScope.class);
        if (object != null) {
            final VariableScope variableScope = (VariableScope)object;
            if (base == null) {
                final String variable = (String)property;
                if (("execution".equals(property) && variableScope instanceof ExecutionEntity) || ("task".equals(property) && variableScope instanceof TaskEntity) || (variableScope instanceof CaseExecutionEntity && ("caseExecution".equals(property) || "execution".equals(property)))) {
                    context.setPropertyResolved(true);
                    return variableScope;
                }
                if ("externalTask".equals(property) && variableScope instanceof ExecutionEntity && ((ExecutionEntity)variableScope).getActivity() != null && ((ExecutionEntity)variableScope).getActivity().getActivityBehavior() instanceof ExternalTaskActivityBehavior) {
                    final List<ExternalTaskEntity> externalTasks = ((ExecutionEntity)variableScope).getExternalTasks();
                    if (externalTasks.size() != 1) {
                        throw new ProcessEngineException("Could not resolve expression to single external task entity.");
                    }
                    context.setPropertyResolved(true);
                    return externalTasks.get(0);
                }
                else {
                    if ("execution".equals(property) && variableScope instanceof TaskEntity) {
                        context.setPropertyResolved(true);
                        return ((TaskEntity)variableScope).getExecution();
                    }
                    if ("authenticatedUserId".equals(property)) {
                        context.setPropertyResolved(true);
                        return Context.getCommandContext().getAuthenticatedUserId();
                    }
                    if (variableScope.hasVariable(variable)) {
                        context.setPropertyResolved(true);
                        return variableScope.getVariable(variable);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        if (base == null) {
            final String variable = (String)property;
            final Object object = context.getContext(VariableScope.class);
            return object != null && !((VariableScope)object).hasVariable(variable);
        }
        return true;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        if (base == null) {
            final String variable = (String)property;
            final Object object = context.getContext(VariableScope.class);
            if (object != null) {
                final VariableScope variableScope = (VariableScope)object;
                if (variableScope.hasVariable(variable)) {
                    variableScope.setVariable(variable, value);
                    context.setPropertyResolved(true);
                }
            }
        }
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext arg0, final Object arg1) {
        return Object.class;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext arg0, final Object arg1) {
        return null;
    }
    
    @Override
    public Class<?> getType(final ELContext arg0, final Object arg1, final Object arg2) {
        return Object.class;
    }
}
