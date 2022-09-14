// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.tree.ReferenceWalker;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.tree.TreeVisitor;
import org.zik.bpm.engine.impl.tree.FlowScopeWalker;

public class CannotAddMultiInstanceBodyValidator implements MigrationInstructionValidator
{
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl targetActivity = instruction.getTargetActivity();
        final FlowScopeWalker flowScopeWalker = new FlowScopeWalker(targetActivity.getFlowScope());
        final MiBodyCollector miBodyCollector = new MiBodyCollector();
        flowScopeWalker.addPreVisitor(miBodyCollector);
        flowScopeWalker.walkWhile(new ReferenceWalker.WalkCondition<ScopeImpl>() {
            @Override
            public boolean isFulfilled(final ScopeImpl element) {
                return element == null || !instructions.getInstructionsByTargetScope(element).isEmpty();
            }
        });
        if (miBodyCollector.firstMiBody != null) {
            report.addFailure("Target activity '" + targetActivity.getId() + "' is a descendant of multi-instance body '" + miBodyCollector.firstMiBody.getId() + "' that is not mapped from the source process definition.");
        }
    }
    
    public static class MiBodyCollector implements TreeVisitor<ScopeImpl>
    {
        protected ScopeImpl firstMiBody;
        
        @Override
        public void visit(final ScopeImpl obj) {
            if (this.firstMiBody == null && obj != null && this.isMiBody(obj)) {
                this.firstMiBody = obj;
            }
        }
        
        protected boolean isMiBody(final ScopeImpl scope) {
            return scope.getActivityBehavior() instanceof MultiInstanceActivityBehavior;
        }
    }
}
