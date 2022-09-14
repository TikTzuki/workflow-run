// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.impl.bpmn.behavior.EventSubProcessActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.bpmn.behavior.CaseCallActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.CallActivityBehavior;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class SameBehaviorInstructionValidator implements MigrationInstructionValidator
{
    public static final List<Set<Class<?>>> EQUIVALENT_BEHAVIORS;
    protected Map<Class<?>, Set<Class<?>>> equivalentBehaviors;
    
    public SameBehaviorInstructionValidator() {
        this(SameBehaviorInstructionValidator.EQUIVALENT_BEHAVIORS);
    }
    
    public SameBehaviorInstructionValidator(final List<Set<Class<?>>> equivalentBehaviors) {
        this.equivalentBehaviors = new HashMap<Class<?>, Set<Class<?>>>();
        for (final Set<Class<?>> equivalenceClass : equivalentBehaviors) {
            for (final Class<?> clazz : equivalenceClass) {
                this.equivalentBehaviors.put(clazz, equivalenceClass);
            }
        }
    }
    
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl sourceActivity = instruction.getSourceActivity();
        final ActivityImpl targetActivity = instruction.getTargetActivity();
        final Class<?> sourceBehaviorClass = sourceActivity.getActivityBehavior().getClass();
        final Class<?> targetBehaviorClass = targetActivity.getActivityBehavior().getClass();
        if (!this.sameBehavior(sourceBehaviorClass, targetBehaviorClass)) {
            report.addFailure("Activities have incompatible types (" + sourceBehaviorClass.getSimpleName() + " is not compatible with " + targetBehaviorClass.getSimpleName() + ")");
        }
    }
    
    protected boolean sameBehavior(final Class<?> sourceBehavior, final Class<?> targetBehavior) {
        if (sourceBehavior == targetBehavior) {
            return true;
        }
        final Set<Class<?>> equivalentBehaviors = this.equivalentBehaviors.get(sourceBehavior);
        return equivalentBehaviors != null && equivalentBehaviors.contains(targetBehavior);
    }
    
    static {
        (EQUIVALENT_BEHAVIORS = new ArrayList<Set<Class<?>>>()).add(CollectionUtil.asHashSet(CallActivityBehavior.class, CaseCallActivityBehavior.class));
        SameBehaviorInstructionValidator.EQUIVALENT_BEHAVIORS.add(CollectionUtil.asHashSet(SubProcessActivityBehavior.class, EventSubProcessActivityBehavior.class));
    }
}
