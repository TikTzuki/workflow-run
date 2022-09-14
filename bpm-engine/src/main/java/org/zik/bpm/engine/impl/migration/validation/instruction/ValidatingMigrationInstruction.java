// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public interface ValidatingMigrationInstruction
{
    ActivityImpl getSourceActivity();
    
    ActivityImpl getTargetActivity();
    
    boolean isUpdateEventTrigger();
    
    MigrationInstruction toMigrationInstruction();
}
