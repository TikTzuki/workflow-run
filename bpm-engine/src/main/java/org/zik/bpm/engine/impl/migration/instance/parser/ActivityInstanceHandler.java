// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import java.util.List;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessElementInstance;
import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.impl.core.delegate.CoreActivityBehavior;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.pvm.delegate.MigrationObserverBehavior;
import org.zik.bpm.engine.impl.migration.instance.MigratingScopeInstance;
import org.zik.bpm.engine.runtime.ActivityInstance;

public class ActivityInstanceHandler implements MigratingInstanceParseHandler<ActivityInstance>
{
    @Override
    public void handle(final MigratingInstanceParseContext parseContext, final ActivityInstance element) {
        MigratingActivityInstance migratingInstance = null;
        final MigrationInstruction applyingInstruction = parseContext.getInstructionFor(element.getActivityId());
        ScopeImpl sourceScope = null;
        ScopeImpl targetScope = null;
        final ExecutionEntity representativeExecution = parseContext.getMapping().getExecution(element);
        if (element.getId().equals(element.getProcessInstanceId())) {
            sourceScope = parseContext.getSourceProcessDefinition();
            targetScope = parseContext.getTargetProcessDefinition();
        }
        else {
            sourceScope = parseContext.getSourceProcessDefinition().findActivity(element.getActivityId());
            if (applyingInstruction != null) {
                final String activityId = applyingInstruction.getTargetActivityId();
                targetScope = parseContext.getTargetProcessDefinition().findActivity(activityId);
            }
        }
        migratingInstance = parseContext.getMigratingProcessInstance().addActivityInstance(applyingInstruction, element, sourceScope, targetScope, representativeExecution);
        final MigratingActivityInstance parentInstance = parseContext.getMigratingActivityInstanceById(element.getParentActivityInstanceId());
        if (parentInstance != null) {
            migratingInstance.setParent(parentInstance);
        }
        final CoreActivityBehavior<?> sourceActivityBehavior = sourceScope.getActivityBehavior();
        if (sourceActivityBehavior instanceof MigrationObserverBehavior) {
            ((MigrationObserverBehavior)sourceActivityBehavior).onParseMigratingInstance(parseContext, migratingInstance);
        }
        parseContext.submit(migratingInstance);
        this.parseTransitionInstances(parseContext, migratingInstance);
        this.parseDependentInstances(parseContext, migratingInstance);
    }
    
    public void parseTransitionInstances(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance migratingInstance) {
        for (final TransitionInstance transitionInstance : migratingInstance.getActivityInstance().getChildTransitionInstances()) {
            parseContext.handleTransitionInstance(transitionInstance);
        }
    }
    
    public void parseDependentInstances(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance migratingInstance) {
        parseContext.handleDependentVariables(migratingInstance, this.collectActivityInstanceVariables(migratingInstance));
        parseContext.handleDependentActivityInstanceJobs(migratingInstance, this.collectActivityInstanceJobs(migratingInstance));
        parseContext.handleDependentEventSubscriptions(migratingInstance, this.collectActivityInstanceEventSubscriptions(migratingInstance));
    }
    
    protected List<VariableInstanceEntity> collectActivityInstanceVariables(final MigratingActivityInstance instance) {
        final List<VariableInstanceEntity> variables = new ArrayList<VariableInstanceEntity>();
        final ExecutionEntity representativeExecution = instance.resolveRepresentativeExecution();
        final ExecutionEntity parentExecution = representativeExecution.getParent();
        final boolean addAllRepresentativeExecutionVariables = instance.getSourceScope().isScope() || representativeExecution.isConcurrent();
        if (addAllRepresentativeExecutionVariables) {
            variables.addAll(representativeExecution.getVariablesInternal());
        }
        else {
            variables.addAll(getConcurrentLocalVariables(representativeExecution));
        }
        final boolean addAnyParentExecutionVariables = parentExecution != null && instance.getSourceScope().isScope();
        if (addAnyParentExecutionVariables) {
            final boolean addAllParentExecutionVariables = parentExecution.isConcurrent();
            if (addAllParentExecutionVariables) {
                variables.addAll(parentExecution.getVariablesInternal());
            }
            else {
                variables.addAll(getConcurrentLocalVariables(parentExecution));
            }
        }
        return variables;
    }
    
    protected List<EventSubscriptionEntity> collectActivityInstanceEventSubscriptions(final MigratingActivityInstance migratingInstance) {
        if (migratingInstance.getSourceScope().isScope()) {
            return migratingInstance.resolveRepresentativeExecution().getEventSubscriptions();
        }
        return Collections.emptyList();
    }
    
    protected List<JobEntity> collectActivityInstanceJobs(final MigratingActivityInstance migratingInstance) {
        if (migratingInstance.getSourceScope().isScope()) {
            return migratingInstance.resolveRepresentativeExecution().getJobs();
        }
        return Collections.emptyList();
    }
    
    public static List<VariableInstanceEntity> getConcurrentLocalVariables(final ExecutionEntity execution) {
        final List<VariableInstanceEntity> variables = new ArrayList<VariableInstanceEntity>();
        for (final VariableInstanceEntity variable : execution.getVariablesInternal()) {
            if (variable.isConcurrentLocal()) {
                variables.add(variable);
            }
        }
        return variables;
    }
}
