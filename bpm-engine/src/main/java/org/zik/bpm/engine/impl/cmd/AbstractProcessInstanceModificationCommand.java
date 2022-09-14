// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import java.util.Set;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.ActivityExecutionTreeMapping;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractProcessInstanceModificationCommand implements Command<Void>
{
    protected String processInstanceId;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    protected boolean externallyTerminated;
    
    public AbstractProcessInstanceModificationCommand(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public void setSkipCustomListeners(final boolean skipCustomListeners) {
        this.skipCustomListeners = skipCustomListeners;
    }
    
    public void setSkipIoMappings(final boolean skipIoMappings) {
        this.skipIoMappings = skipIoMappings;
    }
    
    public void setExternallyTerminated(final boolean externallyTerminated) {
        this.externallyTerminated = externallyTerminated;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    protected ActivityInstance findActivityInstance(final ActivityInstance tree, final String activityInstanceId) {
        if (activityInstanceId.equals(tree.getId())) {
            return tree;
        }
        for (final ActivityInstance child : tree.getChildActivityInstances()) {
            final ActivityInstance matchingChildInstance = this.findActivityInstance(child, activityInstanceId);
            if (matchingChildInstance != null) {
                return matchingChildInstance;
            }
        }
        return null;
    }
    
    protected TransitionInstance findTransitionInstance(final ActivityInstance tree, final String transitionInstanceId) {
        for (final TransitionInstance childTransitionInstance : tree.getChildTransitionInstances()) {
            if (this.matchesRequestedTransitionInstance(childTransitionInstance, transitionInstanceId)) {
                return childTransitionInstance;
            }
        }
        for (final ActivityInstance child : tree.getChildActivityInstances()) {
            final TransitionInstance matchingChildInstance = this.findTransitionInstance(child, transitionInstanceId);
            if (matchingChildInstance != null) {
                return matchingChildInstance;
            }
        }
        return null;
    }
    
    protected boolean matchesRequestedTransitionInstance(final TransitionInstance instance, final String queryInstanceId) {
        boolean match = instance.getId().equals(queryInstanceId);
        if (!match) {
            final ExecutionEntity cachedExecution = Context.getCommandContext().getDbEntityManager().getCachedEntity(ExecutionEntity.class, queryInstanceId);
            if (cachedExecution != null) {
                final ExecutionEntity replacingExecution = cachedExecution.resolveReplacedBy();
                if (replacingExecution != null) {
                    match = replacingExecution.getId().equals(instance.getId());
                }
            }
        }
        return match;
    }
    
    protected ScopeImpl getScopeForActivityInstance(final ProcessDefinitionImpl processDefinition, final ActivityInstance activityInstance) {
        final String scopeId = activityInstance.getActivityId();
        if (processDefinition.getId().equals(scopeId)) {
            return processDefinition;
        }
        return processDefinition.findActivity(scopeId);
    }
    
    protected ExecutionEntity getScopeExecutionForActivityInstance(final ExecutionEntity processInstance, final ActivityExecutionTreeMapping mapping, final ActivityInstance activityInstance) {
        EnsureUtil.ensureNotNull("activityInstance", activityInstance);
        final ProcessDefinitionImpl processDefinition = processInstance.getProcessDefinition();
        final ScopeImpl scope = this.getScopeForActivityInstance(processDefinition, activityInstance);
        final Set<ExecutionEntity> executions = mapping.getExecutions(scope);
        final Set<String> activityInstanceExecutions = new HashSet<String>(Arrays.asList(activityInstance.getExecutionIds()));
        for (final String activityInstanceExecutionId : activityInstance.getExecutionIds()) {
            final ExecutionEntity execution = Context.getCommandContext().getExecutionManager().findExecutionById(activityInstanceExecutionId);
            if (execution.isConcurrent() && execution.hasChildren()) {
                final ExecutionEntity child = execution.getExecutions().get(0);
                activityInstanceExecutions.add(child.getId());
            }
        }
        final Set<ExecutionEntity> retainedExecutionsForInstance = new HashSet<ExecutionEntity>();
        for (final ExecutionEntity execution2 : executions) {
            if (activityInstanceExecutions.contains(execution2.getId())) {
                retainedExecutionsForInstance.add(execution2);
            }
        }
        if (retainedExecutionsForInstance.size() != 1) {
            throw new ProcessEngineException("There are " + retainedExecutionsForInstance.size() + " (!= 1) executions for activity instance " + activityInstance.getId());
        }
        return retainedExecutionsForInstance.iterator().next();
    }
    
    protected String describeFailure(final String detailMessage) {
        return "Cannot perform instruction: " + this.describe() + "; " + detailMessage;
    }
    
    protected abstract String describe();
    
    @Override
    public String toString() {
        return this.describe();
    }
}
