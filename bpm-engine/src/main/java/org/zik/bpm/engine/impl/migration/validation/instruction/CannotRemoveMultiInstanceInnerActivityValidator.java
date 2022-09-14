// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;

public class CannotRemoveMultiInstanceInnerActivityValidator implements MigrationInstructionValidator
{
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl sourceActivity = instruction.getSourceActivity();
        if (this.isMultiInstance(sourceActivity)) {
            final ActivityImpl innerActivity = this.getInnerActivity(sourceActivity);
            if (instructions.getInstructionsBySourceScope(innerActivity).isEmpty()) {
                report.addFailure("Cannot remove the inner activity of a multi-instance body when the body is mapped");
            }
        }
    }
    
    protected boolean isMultiInstance(final ScopeImpl flowScope) {
        return flowScope.getActivityBehavior() instanceof MultiInstanceActivityBehavior;
    }
    
    protected ActivityImpl getInnerActivity(final ActivityImpl multiInstanceBody) {
        final MultiInstanceActivityBehavior activityBehavior = (MultiInstanceActivityBehavior)multiInstanceBody.getActivityBehavior();
        return activityBehavior.getInnerActivity(multiInstanceBody);
    }
}
