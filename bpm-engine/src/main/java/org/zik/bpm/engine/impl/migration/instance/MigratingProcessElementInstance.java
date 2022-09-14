// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;

public abstract class MigratingProcessElementInstance implements MigratingInstance
{
    protected MigrationInstruction migrationInstruction;
    protected ScopeImpl sourceScope;
    protected ScopeImpl targetScope;
    protected ScopeImpl currentScope;
    protected MigratingScopeInstance parentInstance;
    
    public ScopeImpl getSourceScope() {
        return this.sourceScope;
    }
    
    public ScopeImpl getTargetScope() {
        return this.targetScope;
    }
    
    public ScopeImpl getCurrentScope() {
        return this.currentScope;
    }
    
    public MigrationInstruction getMigrationInstruction() {
        return this.migrationInstruction;
    }
    
    public MigratingScopeInstance getParent() {
        return this.parentInstance;
    }
    
    public boolean migratesTo(final ScopeImpl other) {
        return other == this.targetScope;
    }
    
    public abstract void setParent(final MigratingScopeInstance p0);
    
    public abstract void addMigratingDependentInstance(final MigratingInstance p0);
    
    public abstract ExecutionEntity resolveRepresentativeExecution();
    
    public MigratingActivityInstance getClosestAncestorActivityInstance() {
        MigratingScopeInstance ancestorInstance;
        for (ancestorInstance = this.parentInstance; !(ancestorInstance instanceof MigratingActivityInstance); ancestorInstance = ancestorInstance.getParent()) {}
        return (MigratingActivityInstance)ancestorInstance;
    }
}
