// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingCalledCaseInstance implements MigratingInstance
{
    public static final MigrationLogger MIGRATION_LOGGER;
    protected CaseExecutionEntity caseInstance;
    
    public MigratingCalledCaseInstance(final CaseExecutionEntity caseInstance) {
        this.caseInstance = caseInstance;
    }
    
    @Override
    public boolean isDetached() {
        return this.caseInstance.getSuperExecutionId() == null;
    }
    
    @Override
    public void detachState() {
        this.caseInstance.setSuperExecution(null);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance targetActivityInstance) {
        this.caseInstance.setSuperExecution(targetActivityInstance.resolveRepresentativeExecution());
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        throw MigratingCalledCaseInstance.MIGRATION_LOGGER.cannotAttachToTransitionInstance(this);
    }
    
    @Override
    public void migrateState() {
    }
    
    @Override
    public void migrateDependentEntities() {
    }
    
    static {
        MIGRATION_LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
}
