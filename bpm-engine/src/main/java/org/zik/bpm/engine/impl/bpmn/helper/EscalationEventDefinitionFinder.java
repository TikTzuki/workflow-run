// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.helper;

import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.bpmn.parser.EscalationEventDefinition;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.tree.TreeVisitor;

public class EscalationEventDefinitionFinder implements TreeVisitor<PvmScope>
{
    protected EscalationEventDefinition escalationEventDefinition;
    protected final String escalationCode;
    protected final PvmActivity throwEscalationActivity;
    
    public EscalationEventDefinitionFinder(final String escalationCode, final PvmActivity throwEscalationActivity) {
        this.escalationCode = escalationCode;
        this.throwEscalationActivity = throwEscalationActivity;
    }
    
    @Override
    public void visit(final PvmScope scope) {
        final List<EscalationEventDefinition> escalationEventDefinitions = scope.getProperties().get(BpmnProperties.ESCALATION_EVENT_DEFINITIONS);
        this.escalationEventDefinition = this.findMatchingEscalationEventDefinition(escalationEventDefinitions);
    }
    
    protected EscalationEventDefinition findMatchingEscalationEventDefinition(final List<EscalationEventDefinition> escalationEventDefinitions) {
        for (final EscalationEventDefinition escalationEventDefinition : escalationEventDefinitions) {
            if (this.isMatchingEscalationCode(escalationEventDefinition) && !this.isReThrowingEscalationEventSubprocess(escalationEventDefinition)) {
                return escalationEventDefinition;
            }
        }
        return null;
    }
    
    protected boolean isMatchingEscalationCode(final EscalationEventDefinition escalationEventDefinition) {
        final String escalationCode = escalationEventDefinition.getEscalationCode();
        return escalationCode == null || escalationCode.equals(this.escalationCode);
    }
    
    protected boolean isReThrowingEscalationEventSubprocess(final EscalationEventDefinition escalationEventDefinition) {
        final PvmActivity escalationHandler = escalationEventDefinition.getEscalationHandler();
        return escalationHandler.isSubProcessScope() && escalationHandler.equals(this.throwEscalationActivity.getFlowScope());
    }
    
    public EscalationEventDefinition getEscalationEventDefinition() {
        return this.escalationEventDefinition;
    }
}
