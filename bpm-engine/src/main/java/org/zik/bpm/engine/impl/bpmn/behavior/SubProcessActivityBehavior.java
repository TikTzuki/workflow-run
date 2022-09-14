// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.bpmn.helper.CompensationUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;

public class SubProcessActivityBehavior extends AbstractBpmnActivityBehavior implements CompositeActivityBehavior
{
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final PvmActivity activity = execution.getActivity();
        final PvmActivity initialActivity = activity.getProperties().get(BpmnProperties.INITIAL_ACTIVITY);
        EnsureUtil.ensureNotNull("No initial activity found for subprocess " + execution.getActivity().getId(), "initialActivity", initialActivity);
        execution.executeActivity(initialActivity);
    }
    
    @Override
    public void concurrentChildExecutionEnded(final ActivityExecution scopeExecution, final ActivityExecution endedExecution) {
        endedExecution.remove();
        scopeExecution.tryPruneLastConcurrentChild();
        scopeExecution.forceUpdate();
    }
    
    @Override
    public void complete(final ActivityExecution scopeExecution) {
        this.leave(scopeExecution);
    }
    
    @Override
    public void doLeave(final ActivityExecution execution) {
        CompensationUtil.createEventScopeExecution((ExecutionEntity)execution);
        super.doLeave(execution);
    }
}
