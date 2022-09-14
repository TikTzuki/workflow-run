// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import java.util.Iterator;
import org.zik.bpm.engine.impl.util.StringUtil;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;

public class OnlyOnceMappedActivityInstructionValidator implements MigrationInstructionValidator
{
    @Override
    public void validate(final ValidatingMigrationInstruction instruction, final ValidatingMigrationInstructions instructions, final MigrationInstructionValidationReportImpl report) {
        final ActivityImpl sourceActivity = instruction.getSourceActivity();
        final List<ValidatingMigrationInstruction> instructionsForSourceActivity = instructions.getInstructionsBySourceScope(sourceActivity);
        if (instructionsForSourceActivity.size() > 1) {
            this.addFailure(sourceActivity.getId(), instructionsForSourceActivity, report);
        }
    }
    
    protected void addFailure(final String sourceActivityId, final List<ValidatingMigrationInstruction> migrationInstructions, final MigrationInstructionValidationReportImpl report) {
        report.addFailure("There are multiple mappings for source activity id '" + sourceActivityId + "': " + StringUtil.join(new StringUtil.StringIterator<ValidatingMigrationInstruction>(migrationInstructions.iterator()) {
            @Override
            public String next() {
                return ((ValidatingMigrationInstruction)this.iterator.next()).toString();
            }
        }));
    }
}
