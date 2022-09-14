// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.migration.MigrationInstruction;

public class MigrationInstructionImpl implements MigrationInstruction
{
    protected String sourceActivityId;
    protected String targetActivityId;
    protected boolean updateEventTrigger;
    
    public MigrationInstructionImpl(final String sourceActivityId, final String targetActivityId) {
        this(sourceActivityId, targetActivityId, false);
    }
    
    public MigrationInstructionImpl(final String sourceActivityId, final String targetActivityId, final boolean updateEventTrigger) {
        this.updateEventTrigger = false;
        this.sourceActivityId = sourceActivityId;
        this.targetActivityId = targetActivityId;
        this.updateEventTrigger = updateEventTrigger;
    }
    
    @Override
    public String getSourceActivityId() {
        return this.sourceActivityId;
    }
    
    @Override
    public String getTargetActivityId() {
        return this.targetActivityId;
    }
    
    @Override
    public boolean isUpdateEventTrigger() {
        return this.updateEventTrigger;
    }
    
    public void setUpdateEventTrigger(final boolean updateEventTrigger) {
        this.updateEventTrigger = updateEventTrigger;
    }
    
    @Override
    public String toString() {
        return "MigrationInstructionImpl{sourceActivityId='" + this.sourceActivityId + '\'' + ", targetActivityId='" + this.targetActivityId + '\'' + ", updateEventTrigger='" + this.updateEventTrigger + '\'' + '}';
    }
}
