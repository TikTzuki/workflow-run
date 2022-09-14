// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class SequentialMultiInstanceActivityBehavior extends MultiInstanceActivityBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    
    @Override
    protected void createInstances(final ActivityExecution execution, final int nrOfInstances) throws Exception {
        this.prepareScope(execution, nrOfInstances);
        this.setLoopVariable(execution, "nrOfActiveInstances", 1);
        final ActivityImpl innerActivity = this.getInnerActivity(execution.getActivity());
        this.performInstance(execution, innerActivity, 0);
    }
    
    @Override
    public void complete(final ActivityExecution scopeExecution) {
        final int loopCounter = this.getLoopVariable(scopeExecution, "loopCounter") + 1;
        final int nrOfInstances = this.getLoopVariable(scopeExecution, "nrOfInstances");
        final int nrOfCompletedInstances = this.getLoopVariable(scopeExecution, "nrOfCompletedInstances") + 1;
        this.setLoopVariable(scopeExecution, "nrOfCompletedInstances", nrOfCompletedInstances);
        if (loopCounter == nrOfInstances || this.completionConditionSatisfied(scopeExecution)) {
            this.leave(scopeExecution);
        }
        else {
            final PvmActivity innerActivity = this.getInnerActivity(scopeExecution.getActivity());
            this.performInstance(scopeExecution, innerActivity, loopCounter);
        }
    }
    
    @Override
    public void concurrentChildExecutionEnded(final ActivityExecution scopeExecution, final ActivityExecution endedExecution) {
    }
    
    protected void prepareScope(final ActivityExecution scopeExecution, final int totalNumberOfInstances) {
        this.setLoopVariable(scopeExecution, "nrOfInstances", totalNumberOfInstances);
        this.setLoopVariable(scopeExecution, "nrOfCompletedInstances", 0);
    }
    
    @Override
    public List<ActivityExecution> initializeScope(final ActivityExecution scopeExecution, final int nrOfInstances) {
        if (nrOfInstances > 1) {
            SequentialMultiInstanceActivityBehavior.LOG.unsupportedConcurrencyException(scopeExecution.toString(), this.getClass().getSimpleName());
        }
        final List<ActivityExecution> executions = new ArrayList<ActivityExecution>();
        this.prepareScope(scopeExecution, nrOfInstances);
        this.setLoopVariable(scopeExecution, "nrOfActiveInstances", nrOfInstances);
        if (nrOfInstances > 0) {
            this.setLoopVariable(scopeExecution, "loopCounter", 0);
            executions.add(scopeExecution);
        }
        return executions;
    }
    
    @Override
    public ActivityExecution createInnerInstance(final ActivityExecution scopeExecution) {
        if (this.hasLoopVariable(scopeExecution, "nrOfActiveInstances") && this.getLoopVariable(scopeExecution, "nrOfActiveInstances") > 0) {
            throw SequentialMultiInstanceActivityBehavior.LOG.unsupportedConcurrencyException(scopeExecution.toString(), this.getClass().getSimpleName());
        }
        final int nrOfInstances = this.getLoopVariable(scopeExecution, "nrOfInstances");
        this.setLoopVariable(scopeExecution, "loopCounter", nrOfInstances);
        this.setLoopVariable(scopeExecution, "nrOfInstances", nrOfInstances + 1);
        this.setLoopVariable(scopeExecution, "nrOfActiveInstances", 1);
        return scopeExecution;
    }
    
    @Override
    public void destroyInnerInstance(final ActivityExecution scopeExecution) {
        this.removeLoopVariable(scopeExecution, "loopCounter");
        final int nrOfActiveInstances = this.getLoopVariable(scopeExecution, "nrOfActiveInstances");
        this.setLoopVariable(scopeExecution, "nrOfActiveInstances", nrOfActiveInstances - 1);
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
