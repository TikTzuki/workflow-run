// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.bpmn.parser.EscalationEventDefinition;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.bpmn.helper.EscalationHandler;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.parser.Escalation;

public class ThrowEscalationEventActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected final Escalation escalation;
    
    public ThrowEscalationEventActivityBehavior(final Escalation escalation) {
        this.escalation = escalation;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final PvmActivity currentActivity = execution.getActivity();
        final EscalationEventDefinition escalationEventDefinition = EscalationHandler.executeEscalation(execution, this.escalation.getEscalationCode());
        if (escalationEventDefinition == null || !escalationEventDefinition.isCancelActivity()) {
            this.leaveExecution(execution, currentActivity, escalationEventDefinition);
        }
    }
    
    protected void leaveExecution(final ActivityExecution execution, final PvmActivity currentActivity, final EscalationEventDefinition escalationEventDefinition) {
        final ExecutionEntity replacingExecution = ((ExecutionEntity)execution).getReplacedBy();
        final ExecutionEntity leavingExecution = (ExecutionEntity)((replacingExecution != null) ? replacingExecution : execution);
        this.leave(leavingExecution);
    }
}
