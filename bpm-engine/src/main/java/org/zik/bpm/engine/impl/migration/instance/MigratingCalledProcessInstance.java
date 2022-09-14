// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingCalledProcessInstance implements MigratingInstance
{
    public static final MigrationLogger MIGRATION_LOGGER;
    protected ExecutionEntity processInstance;
    
    public MigratingCalledProcessInstance(final ExecutionEntity processInstance) {
        this.processInstance = processInstance;
    }
    
    @Override
    public boolean isDetached() {
        return this.processInstance.getSuperExecutionId() == null;
    }
    
    @Override
    public void detachState() {
        this.processInstance.setSuperExecution(null);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance targetActivityInstance) {
        this.processInstance.setSuperExecution(targetActivityInstance.resolveRepresentativeExecution());
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        throw MigratingCalledProcessInstance.MIGRATION_LOGGER.cannotAttachToTransitionInstance(this);
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
