// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.EventSubProcessStartEventActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.BoundaryEventActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;

public class SameEventTypeValidator implements MigrationInstructionValidator
{
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl sourceActivity = instruction.getSourceActivity();
        final ActivityImpl targetActivity = instruction.getTargetActivity();
        if (this.isEvent(sourceActivity) && this.isEvent(targetActivity)) {
            final String sourceType = sourceActivity.getProperties().get(BpmnProperties.TYPE);
            final String targetType = targetActivity.getProperties().get(BpmnProperties.TYPE);
            if (!sourceType.equals(targetType)) {
                report.addFailure("Events are not of the same type (" + sourceType + " != " + targetType + ")");
            }
        }
    }
    
    protected boolean isEvent(final ActivityImpl activity) {
        final ActivityBehavior behavior = activity.getActivityBehavior();
        return behavior instanceof BoundaryEventActivityBehavior || behavior instanceof EventSubProcessStartEventActivityBehavior;
    }
}
