// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.Iterator;
import org.zik.bpm.engine.runtime.Incident;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.pvm.runtime.CompensationBehavior;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.TransitionInstanceImpl;
import org.zik.bpm.engine.impl.persistence.entity.ActivityInstanceImpl;
import java.util.HashMap;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetActivityInstanceCmd implements Command<ActivityInstance>
{
    protected String processInstanceId;
    
    public GetActivityInstanceCmd(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    @Override
    public ActivityInstance execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("processInstanceId", (Object)this.processInstanceId);
        final List<ExecutionEntity> executionList = this.loadProcessInstance(this.processInstanceId, commandContext);
        if (executionList.isEmpty()) {
            return null;
        }
        this.checkGetActivityInstance(this.processInstanceId, commandContext);
        final List<ExecutionEntity> nonEventScopeExecutions = this.filterNonEventScopeExecutions(executionList);
        final List<ExecutionEntity> leaves = this.filterLeaves(nonEventScopeExecutions);
        this.orderById(leaves);
        final ExecutionEntity processInstance = this.filterProcessInstance(executionList);
        if (processInstance.isEnded()) {
            return null;
        }
        final Map<String, List<Incident>> incidents = this.groupIncidentIdsByExecutionId(commandContext);
        final ActivityInstanceImpl processActInst = this.createActivityInstance(processInstance, processInstance.getProcessDefinition(), this.processInstanceId, null, incidents);
        final Map<String, ActivityInstanceImpl> activityInstances = new HashMap<String, ActivityInstanceImpl>();
        activityInstances.put(this.processInstanceId, processActInst);
        final Map<String, TransitionInstanceImpl> transitionInstances = new HashMap<String, TransitionInstanceImpl>();
        for (final ExecutionEntity leaf : leaves) {
            if (leaf.getActivity() == null) {
                continue;
            }
            final Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping = leaf.createActivityExecutionMapping();
            final Map<ScopeImpl, PvmExecutionImpl> scopeInstancesToCreate = new HashMap<ScopeImpl, PvmExecutionImpl>(activityExecutionMapping);
            if (leaf.getActivityInstanceId() != null) {
                if (!CompensationBehavior.isCompensationThrowing(leaf) || LegacyBehavior.isCompensationThrowing(leaf, activityExecutionMapping)) {
                    String parentActivityInstanceId = null;
                    parentActivityInstanceId = activityExecutionMapping.get(leaf.getActivity().getFlowScope()).getParentActivityInstanceId();
                    final ActivityInstanceImpl leafInstance = this.createActivityInstance(leaf, leaf.getActivity(), leaf.getActivityInstanceId(), parentActivityInstanceId, incidents);
                    activityInstances.put(leafInstance.getId(), leafInstance);
                    scopeInstancesToCreate.remove(leaf.getActivity());
                }
            }
            else {
                final TransitionInstanceImpl transitionInstance = this.createTransitionInstance(leaf, incidents);
                transitionInstances.put(transitionInstance.getId(), transitionInstance);
                scopeInstancesToCreate.remove(leaf.getActivity());
            }
            LegacyBehavior.removeLegacyNonScopesFromMapping(scopeInstancesToCreate);
            scopeInstancesToCreate.remove(leaf.getProcessDefinition());
            for (final Map.Entry<ScopeImpl, PvmExecutionImpl> scopeExecutionEntry : scopeInstancesToCreate.entrySet()) {
                final ScopeImpl scope = scopeExecutionEntry.getKey();
                final PvmExecutionImpl scopeExecution = scopeExecutionEntry.getValue();
                String activityInstanceId = null;
                String parentActivityInstanceId2 = null;
                activityInstanceId = scopeExecution.getParentActivityInstanceId();
                parentActivityInstanceId2 = activityExecutionMapping.get(scope.getFlowScope()).getParentActivityInstanceId();
                if (activityInstances.containsKey(activityInstanceId)) {
                    continue;
                }
                final ActivityInstanceImpl scopeInstance = this.createActivityInstance(scopeExecution, scope, activityInstanceId, parentActivityInstanceId2, incidents);
                activityInstances.put(activityInstanceId, scopeInstance);
            }
        }
        LegacyBehavior.repairParentRelationships(activityInstances.values(), this.processInstanceId);
        this.populateChildInstances(activityInstances, transitionInstances);
        return processActInst;
    }
    
    protected void checkGetActivityInstance(final String processInstanceId, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessInstance(processInstanceId);
        }
    }
    
    protected void orderById(final List<ExecutionEntity> leaves) {
        Collections.sort(leaves, ExecutionIdComparator.INSTANCE);
    }
    
    protected ActivityInstanceImpl createActivityInstance(final PvmExecutionImpl scopeExecution, final ScopeImpl scope, final String activityInstanceId, final String parentActivityInstanceId, final Map<String, List<Incident>> incidentsByExecution) {
        final ActivityInstanceImpl actInst = new ActivityInstanceImpl();
        actInst.setId(activityInstanceId);
        actInst.setParentActivityInstanceId(parentActivityInstanceId);
        actInst.setProcessInstanceId(scopeExecution.getProcessInstanceId());
        actInst.setProcessDefinitionId(scopeExecution.getProcessDefinitionId());
        actInst.setBusinessKey(scopeExecution.getBusinessKey());
        actInst.setActivityId(scope.getId());
        String name = scope.getName();
        if (name == null) {
            name = (String)scope.getProperty("name");
        }
        actInst.setActivityName(name);
        if (scope.getId().equals(scopeExecution.getProcessDefinition().getId())) {
            actInst.setActivityType("processDefinition");
        }
        else {
            actInst.setActivityType((String)scope.getProperty("type"));
        }
        final List<String> executionIds = new ArrayList<String>();
        final List<String> incidentIds = new ArrayList<String>();
        final List<Incident> incidents = new ArrayList<Incident>();
        executionIds.add(scopeExecution.getId());
        final ActivityImpl executionActivity = scopeExecution.getActivity();
        if (executionActivity == null || executionActivity == scope) {
            incidentIds.addAll(this.getIncidentIds(incidentsByExecution, scopeExecution));
            incidents.addAll(this.getIncidents(incidentsByExecution, scopeExecution));
        }
        for (final PvmExecutionImpl childExecution : scopeExecution.getNonEventScopeExecutions()) {
            if (childExecution.isConcurrent() && childExecution.getActivityId() == null) {
                executionIds.add(childExecution.getId());
                incidentIds.addAll(this.getIncidentIds(incidentsByExecution, childExecution));
                incidents.addAll(this.getIncidents(incidentsByExecution, childExecution));
            }
        }
        actInst.setExecutionIds(executionIds.toArray(new String[executionIds.size()]));
        actInst.setIncidentIds(incidentIds.toArray(new String[incidentIds.size()]));
        actInst.setIncidents(incidents.toArray(new Incident[0]));
        return actInst;
    }
    
    protected TransitionInstanceImpl createTransitionInstance(final PvmExecutionImpl execution, final Map<String, List<Incident>> incidentsByExecution) {
        final TransitionInstanceImpl transitionInstance = new TransitionInstanceImpl();
        transitionInstance.setId(execution.getId());
        transitionInstance.setParentActivityInstanceId(execution.getParentActivityInstanceId());
        transitionInstance.setProcessInstanceId(execution.getProcessInstanceId());
        transitionInstance.setProcessDefinitionId(execution.getProcessDefinitionId());
        transitionInstance.setExecutionId(execution.getId());
        transitionInstance.setActivityId(execution.getActivityId());
        final ActivityImpl activity = execution.getActivity();
        if (activity != null) {
            String name = activity.getName();
            if (name == null) {
                name = (String)activity.getProperty("name");
            }
            transitionInstance.setActivityName(name);
            transitionInstance.setActivityType((String)activity.getProperty("type"));
        }
        final List<String> incidentIdList = this.getIncidentIds(incidentsByExecution, execution);
        final List<Incident> incidents = this.getIncidents(incidentsByExecution, execution);
        transitionInstance.setIncidentIds(incidentIdList.toArray(new String[0]));
        transitionInstance.setIncidents(incidents.toArray(new Incident[0]));
        return transitionInstance;
    }
    
    protected void populateChildInstances(final Map<String, ActivityInstanceImpl> activityInstances, final Map<String, TransitionInstanceImpl> transitionInstances) {
        final Map<ActivityInstanceImpl, List<ActivityInstanceImpl>> childActivityInstances = new HashMap<ActivityInstanceImpl, List<ActivityInstanceImpl>>();
        final Map<ActivityInstanceImpl, List<TransitionInstanceImpl>> childTransitionInstances = new HashMap<ActivityInstanceImpl, List<TransitionInstanceImpl>>();
        for (final ActivityInstanceImpl instance : activityInstances.values()) {
            if (instance.getParentActivityInstanceId() != null) {
                final ActivityInstanceImpl parentInstance = activityInstances.get(instance.getParentActivityInstanceId());
                if (parentInstance == null) {
                    throw new ProcessEngineException("No parent activity instance with id " + instance.getParentActivityInstanceId() + " generated");
                }
                this.putListElement(childActivityInstances, parentInstance, instance);
            }
        }
        for (final TransitionInstanceImpl instance2 : transitionInstances.values()) {
            if (instance2.getParentActivityInstanceId() != null) {
                final ActivityInstanceImpl parentInstance = activityInstances.get(instance2.getParentActivityInstanceId());
                if (parentInstance == null) {
                    throw new ProcessEngineException("No parent activity instance with id " + instance2.getParentActivityInstanceId() + " generated");
                }
                this.putListElement(childTransitionInstances, parentInstance, instance2);
            }
        }
        for (final Map.Entry<ActivityInstanceImpl, List<ActivityInstanceImpl>> entry : childActivityInstances.entrySet()) {
            final ActivityInstanceImpl instance3 = entry.getKey();
            final List<ActivityInstanceImpl> childInstances = entry.getValue();
            if (childInstances != null) {
                instance3.setChildActivityInstances(childInstances.toArray(new ActivityInstanceImpl[childInstances.size()]));
            }
        }
        for (final Map.Entry<ActivityInstanceImpl, List<TransitionInstanceImpl>> entry2 : childTransitionInstances.entrySet()) {
            final ActivityInstanceImpl instance3 = entry2.getKey();
            final List<TransitionInstanceImpl> childInstances2 = entry2.getValue();
            if (childTransitionInstances != null) {
                instance3.setChildTransitionInstances(childInstances2.toArray(new TransitionInstanceImpl[childInstances2.size()]));
            }
        }
    }
    
    protected <S, T> void putListElement(final Map<S, List<T>> mapOfLists, final S key, final T listElement) {
        List<T> list = mapOfLists.get(key);
        if (list == null) {
            list = new ArrayList<T>();
            mapOfLists.put(key, list);
        }
        list.add(listElement);
    }
    
    protected ExecutionEntity filterProcessInstance(final List<ExecutionEntity> executionList) {
        for (final ExecutionEntity execution : executionList) {
            if (execution.isProcessInstanceExecution()) {
                return execution;
            }
        }
        throw new ProcessEngineException("Could not determine process instance execution");
    }
    
    protected List<ExecutionEntity> filterLeaves(final List<ExecutionEntity> executionList) {
        final List<ExecutionEntity> leaves = new ArrayList<ExecutionEntity>();
        for (final ExecutionEntity execution : executionList) {
            if (execution.getNonEventScopeExecutions().isEmpty() || CompensationBehavior.isCompensationThrowing(execution)) {
                leaves.add(execution);
            }
        }
        return leaves;
    }
    
    protected List<ExecutionEntity> filterNonEventScopeExecutions(final List<ExecutionEntity> executionList) {
        final List<ExecutionEntity> nonEventScopeExecutions = new ArrayList<ExecutionEntity>();
        for (final ExecutionEntity execution : executionList) {
            if (!execution.isEventScope()) {
                nonEventScopeExecutions.add(execution);
            }
        }
        return nonEventScopeExecutions;
    }
    
    protected List<ExecutionEntity> loadProcessInstance(final String processInstanceId, final CommandContext commandContext) {
        List<ExecutionEntity> result = null;
        final List<ExecutionEntity> cachedExecutions = commandContext.getDbEntityManager().getCachedEntitiesByType(ExecutionEntity.class);
        for (final ExecutionEntity executionEntity : cachedExecutions) {
            if (processInstanceId.equals(executionEntity.getProcessInstanceId())) {
                result = new ArrayList<ExecutionEntity>();
                final ExecutionEntity processInstance = executionEntity.getProcessInstance();
                result.add(processInstance);
                this.loadChildExecutionsFromCache(processInstance, result);
                break;
            }
        }
        if (result == null) {
            result = this.loadFromDb(processInstanceId, commandContext);
        }
        return result;
    }
    
    protected List<ExecutionEntity> loadFromDb(final String processInstanceId, final CommandContext commandContext) {
        final List<ExecutionEntity> executions = commandContext.getExecutionManager().findExecutionsByProcessInstanceId(processInstanceId);
        final ExecutionEntity processInstance = commandContext.getExecutionManager().findExecutionById(processInstanceId);
        if (processInstance != null) {
            processInstance.restoreProcessInstance(executions, null, null, null, null, null, null);
        }
        return executions;
    }
    
    protected void loadChildExecutionsFromCache(final ExecutionEntity execution, final List<ExecutionEntity> childExecutions) {
        final List<ExecutionEntity> childrenOfThisExecution = execution.getExecutions();
        if (childrenOfThisExecution != null) {
            childExecutions.addAll(childrenOfThisExecution);
            for (final ExecutionEntity child : childrenOfThisExecution) {
                this.loadChildExecutionsFromCache(child, childExecutions);
            }
        }
    }
    
    protected Map<String, List<Incident>> groupIncidentIdsByExecutionId(final CommandContext commandContext) {
        final List<IncidentEntity> incidents = commandContext.getIncidentManager().findIncidentsByProcessInstance(this.processInstanceId);
        final Map<String, List<Incident>> result = new HashMap<String, List<Incident>>();
        for (final IncidentEntity incidentEntity : incidents) {
            CollectionUtil.addToMapOfLists(result, incidentEntity.getExecutionId(), incidentEntity);
        }
        return result;
    }
    
    protected List<String> getIncidentIds(final Map<String, List<Incident>> incidents, final PvmExecutionImpl execution) {
        final List<String> incidentIds = new ArrayList<String>();
        final List<Incident> incidentList = incidents.get(execution.getId());
        if (incidentList != null) {
            for (final Incident incident : incidentList) {
                incidentIds.add(incident.getId());
            }
            return incidentIds;
        }
        return Collections.emptyList();
    }
    
    protected List<Incident> getIncidents(final Map<String, List<Incident>> incidents, final PvmExecutionImpl execution) {
        final List<Incident> incidentList = incidents.get(execution.getId());
        if (incidentList != null) {
            return incidentList;
        }
        return Collections.emptyList();
    }
    
    public static class ExecutionIdComparator implements Comparator<ExecutionEntity>
    {
        public static final ExecutionIdComparator INSTANCE;
        
        @Override
        public int compare(final ExecutionEntity o1, final ExecutionEntity o2) {
            return o1.getId().compareTo(o2.getId());
        }
        
        static {
            INSTANCE = new ExecutionIdComparator();
        }
    }
}
