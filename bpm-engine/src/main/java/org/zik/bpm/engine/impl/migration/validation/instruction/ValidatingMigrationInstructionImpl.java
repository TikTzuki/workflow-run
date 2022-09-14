// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instruction;

import org.zik.bpm.engine.impl.migration.MigrationInstructionImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public class ValidatingMigrationInstructionImpl implements ValidatingMigrationInstruction
{
    protected ActivityImpl sourceActivity;
    protected ActivityImpl targetActivity;
    protected boolean updateEventTrigger;
    
    public ValidatingMigrationInstructionImpl(final ActivityImpl sourceActivity, final ActivityImpl targetActivity, final boolean updateEventTrigger) {
        this.updateEventTrigger = false;
        this.sourceActivity = sourceActivity;
        this.targetActivity = targetActivity;
        this.updateEventTrigger = updateEventTrigger;
    }
    
    @Override
    public ActivityImpl getSourceActivity() {
        return this.sourceActivity;
    }
    
    @Override
    public ActivityImpl getTargetActivity() {
        return this.targetActivity;
    }
    
    @Override
    public boolean isUpdateEventTrigger() {
        return this.updateEventTrigger;
    }
    
    @Override
    public MigrationInstruction toMigrationInstruction() {
        return new MigrationInstructionImpl(this.sourceActivity.getId(), this.targetActivity.getId(), this.updateEventTrigger);
    }
    
    @Override
    public String toString() {
        return "ValidatingMigrationInstructionImpl{sourceActivity=" + this.sourceActivity + ", targetActivity=" + this.targetActivity + '}';
    }
}
