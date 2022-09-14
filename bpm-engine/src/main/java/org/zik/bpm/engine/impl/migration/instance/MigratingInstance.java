// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

public interface MigratingInstance
{
    boolean isDetached();
    
    void detachState();
    
    void attachState(final MigratingScopeInstance p0);
    
    void attachState(final MigratingTransitionInstance p0);
    
    void migrateState();
    
    void migrateDependentEntities();
}
