// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.pvm.runtime.CompensationBehavior;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.List;
import java.util.Iterator;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.runtime.ActivityInstance;
import java.util.HashSet;
import java.util.HashMap;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Set;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Map;

public class ActivityExecutionTreeMapping
{
    protected Map<ScopeImpl, Set<ExecutionEntity>> activityExecutionMapping;
    protected CommandContext commandContext;
    protected String processInstanceId;
    protected ProcessDefinitionImpl processDefinition;
    
    public ActivityExecutionTreeMapping(final CommandContext commandContext, final String processInstanceId) {
        this.activityExecutionMapping = new HashMap<ScopeImpl, Set<ExecutionEntity>>();
        this.commandContext = commandContext;
        this.processInstanceId = processInstanceId;
        this.initialize();
    }
    
    protected void submitExecution(final ExecutionEntity execution, final ScopeImpl scope) {
        this.getExecutions(scope).add(execution);
    }
    
    public Set<ExecutionEntity> getExecutions(final ScopeImpl activity) {
        Set<ExecutionEntity> executionsForActivity = this.activityExecutionMapping.get(activity);
        if (executionsForActivity == null) {
            executionsForActivity = new HashSet<ExecutionEntity>();
            this.activityExecutionMapping.put(activity, executionsForActivity);
        }
        return executionsForActivity;
    }
    
    public ExecutionEntity getExecution(final ActivityInstance activityInstance) {
        ScopeImpl scope = null;
        if (activityInstance.getId().equals(activityInstance.getProcessInstanceId())) {
            scope = this.processDefinition;
        }
        else {
            scope = this.processDefinition.findActivity(activityInstance.getActivityId());
        }
        return this.intersect(this.getExecutions(scope), activityInstance.getExecutionIds());
    }
    
    protected ExecutionEntity intersect(final Set<ExecutionEntity> executions, final String[] executionIds) {
        final Set<String> executionIdSet = new HashSet<String>();
        for (final String executionId : executionIds) {
            executionIdSet.add(executionId);
        }
        for (final ExecutionEntity execution : executions) {
            if (executionIdSet.contains(execution.getId())) {
                return execution;
            }
        }
        throw new ProcessEngineException("Could not determine execution");
    }
    
    protected void initialize() {
        final ExecutionEntity processInstance = this.commandContext.getExecutionManager().findExecutionById(this.processInstanceId);
        this.processDefinition = processInstance.getProcessDefinition();
        final List<ExecutionEntity> executions = this.fetchExecutionsForProcessInstance(processInstance);
        executions.add(processInstance);
        final List<ExecutionEntity> leaves = this.findLeaves(executions);
        this.assignExecutionsToActivities(leaves);
    }
    
    protected void assignExecutionsToActivities(final List<ExecutionEntity> leaves) {
        for (final ExecutionEntity leaf : leaves) {
            final ScopeImpl activity = leaf.getActivity();
            if (activity != null) {
                if (leaf.getActivityInstanceId() != null) {
                    EnsureUtil.ensureNotNull("activity", activity);
                    this.submitExecution(leaf, activity);
                }
                this.mergeScopeExecutions(leaf);
            }
            else {
                if (!leaf.isProcessInstanceExecution()) {
                    continue;
                }
                this.submitExecution(leaf, leaf.getProcessDefinition());
            }
        }
    }
    
    protected void mergeScopeExecutions(final ExecutionEntity leaf) {
        final Map<ScopeImpl, PvmExecutionImpl> mapping = leaf.createActivityExecutionMapping();
        for (final Map.Entry<ScopeImpl, PvmExecutionImpl> mappingEntry : mapping.entrySet()) {
            final ScopeImpl scope = mappingEntry.getKey();
            final ExecutionEntity scopeExecution = mappingEntry.getValue();
            this.submitExecution(scopeExecution, scope);
        }
    }
    
    protected List<ExecutionEntity> fetchExecutionsForProcessInstance(final ExecutionEntity execution) {
        final List<ExecutionEntity> executions = new ArrayList<ExecutionEntity>();
        executions.addAll(execution.getExecutions());
        for (final ExecutionEntity child : execution.getExecutions()) {
            executions.addAll(this.fetchExecutionsForProcessInstance(child));
        }
        return executions;
    }
    
    protected List<ExecutionEntity> findLeaves(final List<ExecutionEntity> executions) {
        final List<ExecutionEntity> leaves = new ArrayList<ExecutionEntity>();
        for (final ExecutionEntity execution : executions) {
            if (this.isLeaf(execution)) {
                leaves.add(execution);
            }
        }
        return leaves;
    }
    
    protected boolean isLeaf(final ExecutionEntity execution) {
        return CompensationBehavior.isCompensationThrowing(execution) || (!execution.isEventScope() && execution.getNonEventScopeExecutions().isEmpty());
    }
}
