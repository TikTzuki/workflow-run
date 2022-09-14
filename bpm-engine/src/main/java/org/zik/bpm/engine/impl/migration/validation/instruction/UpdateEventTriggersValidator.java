// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public class UpdateEventTriggersValidator implements MigrationInstructionValidator
{
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl sourceActivity = instruction.getSourceActivity();
        if (instruction.isUpdateEventTrigger() && !definesPersistentEventTrigger(sourceActivity)) {
            report.addFailure("Cannot update event trigger because the activity does not define a persistent event trigger");
        }
    }
    
    public static boolean definesPersistentEventTrigger(final ActivityImpl activity) {
        final ScopeImpl eventScope = activity.getEventScope();
        return eventScope != null && (TimerDeclarationImpl.getDeclarationsForScope(eventScope).containsKey(activity.getId()) || EventSubscriptionDeclaration.getDeclarationsForScope(eventScope).containsKey(activity.getId()) || TimerDeclarationImpl.getTimeoutListenerDeclarationsForScope(eventScope).containsKey(activity.getId()));
    }
}
