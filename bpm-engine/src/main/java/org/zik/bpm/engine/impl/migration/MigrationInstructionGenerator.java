// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.impl.migration.validation.instruction.ValidatingMigrationInstructions;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.migration.validation.instruction.MigrationInstructionValidator;
import org.zik.bpm.engine.impl.migration.validation.activity.MigrationActivityValidator;
import java.util.List;

public interface MigrationInstructionGenerator
{
    MigrationInstructionGenerator migrationActivityValidators(final List<MigrationActivityValidator> p0);
    
    MigrationInstructionGenerator migrationInstructionValidators(final List<MigrationInstructionValidator> p0);
    
    ValidatingMigrationInstructions generate(final ProcessDefinitionImpl p0, final ProcessDefinitionImpl p1, final boolean p2);
}
