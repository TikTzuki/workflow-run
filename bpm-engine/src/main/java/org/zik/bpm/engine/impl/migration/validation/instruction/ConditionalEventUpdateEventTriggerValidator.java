// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.bpmn.behavior.ConditionalEventBehavior;

public class ConditionalEventUpdateEventTriggerValidator implements MigrationInstructionValidator
{
    public static final String MIGRATION_CONDITIONAL_VALIDATION_ERROR_MSG = "Conditional event has to migrate with update event trigger.";
    
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl sourceActivity = instruction.getSourceActivity();
        if (sourceActivity.getActivityBehavior() instanceof ConditionalEventBehavior && !instruction.isUpdateEventTrigger()) {
            report.addFailure("Conditional event has to migrate with update event trigger.");
        }
    }
}
