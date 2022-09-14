// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.jobexecutor.TimerDeclarationImpl;

public class EmergingJobInstance implements EmergingInstance
{
    protected TimerDeclarationImpl timerDeclaration;
    
    public EmergingJobInstance(final TimerDeclarationImpl timerDeclaration) {
        this.timerDeclaration = timerDeclaration;
    }
    
    @Override
    public void create(final ExecutionEntity scopeExecution) {
        this.timerDeclaration.createTimer(scopeExecution);
    }
}
