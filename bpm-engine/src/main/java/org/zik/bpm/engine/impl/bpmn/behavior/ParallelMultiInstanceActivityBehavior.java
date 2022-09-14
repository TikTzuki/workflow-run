// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.migration.instance.parser.MigratingInstanceParseContext;
import java.util.Iterator;
import org.zik.bpm.engine.impl.pvm.runtime.Callback;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Collection;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.MigrationObserverBehavior;

public class ParallelMultiInstanceActivityBehavior extends MultiInstanceActivityBehavior implements MigrationObserverBehavior
{
    @Override
    protected void createInstances(final ActivityExecution execution, final int nrOfInstances) throws Exception {
        final PvmActivity innerActivity = this.getInnerActivity(execution.getActivity());
        this.prepareScopeExecution(execution, nrOfInstances);
        final List<ActivityExecution> concurrentExecutions = new ArrayList<ActivityExecution>();
        for (int i = 0; i < nrOfInstances; ++i) {
            concurrentExecutions.add(this.createConcurrentExecution(execution));
        }
        for (int i = nrOfInstances - 1; i >= 0; --i) {
            final ActivityExecution activityExecution = concurrentExecutions.get(i);
            this.performInstance(activityExecution, innerActivity, i);
        }
    }
    
    protected void prepareScopeExecution(final ActivityExecution scopeExecution, final int nrOfInstances) {
        this.setLoopVariable(scopeExecution, "nrOfInstances", nrOfInstances);
        this.setLoopVariable(scopeExecution, "nrOfCompletedInstances", 0);
        this.setLoopVariable(scopeExecution, "nrOfActiveInstances", nrOfInstances);
        scopeExecution.setActivity(null);
        scopeExecution.inactivate();
    }
    
    protected ActivityExecution createConcurrentExecution(final ActivityExecution scopeExecution) {
        final ActivityExecution concurrentChild = scopeExecution.createExecution();
        scopeExecution.forceUpdate();
        concurrentChild.setConcurrent(true);
        concurrentChild.setScope(false);
        return concurrentChild;
    }
    
    @Override
    public void concurrentChildExecutionEnded(final ActivityExecution scopeExecution, final ActivityExecution endedExecution) {
        final int nrOfCompletedInstances = this.getLoopVariable(scopeExecution, "nrOfCompletedInstances") + 1;
        this.setLoopVariable(scopeExecution, "nrOfCompletedInstances", nrOfCompletedInstances);
        final int nrOfActiveInstances = this.getLoopVariable(scopeExecution, "nrOfActiveInstances") - 1;
        this.setLoopVariable(scopeExecution, "nrOfActiveInstances", nrOfActiveInstances);
        endedExecution.inactivate();
        endedExecution.setActivityInstanceId(null);
        scopeExecution.forceUpdate();
        if (this.completionConditionSatisfied(endedExecution) || this.allExecutionsEnded(scopeExecution, endedExecution)) {
            final ArrayList<ActivityExecution> childExecutions = new ArrayList<ActivityExecution>(((PvmExecutionImpl)scopeExecution).getNonEventScopeExecutions());
            for (final ActivityExecution childExecution : childExecutions) {
                if (childExecution.isActive() || childExecution.getActivity() == null) {
                    ((PvmExecutionImpl)childExecution).deleteCascade("Multi instance completion condition satisfied.");
                }
                else {
                    childExecution.remove();
                }
            }
            scopeExecution.setActivity((PvmActivity)endedExecution.getActivity().getFlowScope());
            scopeExecution.setActive(true);
            this.leave(scopeExecution);
        }
        else {
            ((ExecutionEntity)scopeExecution).dispatchDelayedEventsAndPerformOperation((Callback<PvmExecutionImpl, Void>)null);
        }
    }
    
    protected boolean allExecutionsEnded(final ActivityExecution scopeExecution, final ActivityExecution endedExecution) {
        final int numberOfInactiveConcurrentExecutions = endedExecution.findInactiveConcurrentExecutions(endedExecution.getActivity()).size();
        final int concurrentExecutions = scopeExecution.getExecutions().size();
        return this.getLocalLoopVariable(scopeExecution, "nrOfActiveInstances") <= 0 && numberOfInactiveConcurrentExecutions == concurrentExecutions;
    }
    
    @Override
    public void complete(final ActivityExecution scopeExecution) {
    }
    
    @Override
    public List<ActivityExecution> initializeScope(final ActivityExecution scopeExecution, final int numberOfInstances) {
        this.prepareScopeExecution(scopeExecution, numberOfInstances);
        final List<ActivityExecution> executions = new ArrayList<ActivityExecution>();
        for (int i = 0; i < numberOfInstances; ++i) {
            final ActivityExecution concurrentChild = this.createConcurrentExecution(scopeExecution);
            this.setLoopVariable(concurrentChild, "loopCounter", i);
            executions.add(concurrentChild);
        }
        return executions;
    }
    
    @Override
    public ActivityExecution createInnerInstance(final ActivityExecution scopeExecution) {
        final ActivityExecution concurrentChild = this.createConcurrentExecution(scopeExecution);
        final int nrOfInstances = this.getLoopVariable(scopeExecution, "nrOfInstances");
        this.setLoopVariable(scopeExecution, "nrOfInstances", nrOfInstances + 1);
        final int nrOfActiveInstances = this.getLoopVariable(scopeExecution, "nrOfActiveInstances");
        this.setLoopVariable(scopeExecution, "nrOfActiveInstances", nrOfActiveInstances + 1);
        this.setLoopVariable(concurrentChild, "loopCounter", nrOfInstances);
        return concurrentChild;
    }
    
    @Override
    public void destroyInnerInstance(final ActivityExecution concurrentExecution) {
        final ActivityExecution scopeExecution = concurrentExecution.getParent();
        concurrentExecution.remove();
        scopeExecution.forceUpdate();
        final int nrOfActiveInstances = this.getLoopVariable(scopeExecution, "nrOfActiveInstances");
        this.setLoopVariable(scopeExecution, "nrOfActiveInstances", nrOfActiveInstances - 1);
    }
    
    @Override
    public void migrateScope(final ActivityExecution scopeExecution) {
        for (final ActivityExecution child : scopeExecution.getExecutions()) {
            if (!child.isActive()) {
                ((PvmExecutionImpl)child).setProcessDefinition(((PvmExecutionImpl)scopeExecution).getProcessDefinition());
            }
        }
    }
    
    @Override
    public void onParseMigratingInstance(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance migratingInstance) {
        final ExecutionEntity scopeExecution = migratingInstance.resolveRepresentativeExecution();
        final List<ActivityExecution> concurrentInActiveExecutions = scopeExecution.findInactiveChildExecutions(this.getInnerActivity((PvmActivity)migratingInstance.getSourceScope()));
        for (final ActivityExecution execution : concurrentInActiveExecutions) {
            for (final VariableInstanceEntity variable : ((ExecutionEntity)execution).getVariablesInternal()) {
                parseContext.consume(variable);
            }
        }
    }
}
