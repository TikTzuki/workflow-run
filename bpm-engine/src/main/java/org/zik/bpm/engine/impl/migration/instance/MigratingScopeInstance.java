// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import java.util.Collection;

public abstract class MigratingScopeInstance extends MigratingProcessElementInstance
{
    public abstract void removeChild(final MigratingScopeInstance p0);
    
    public abstract void addChild(final MigratingScopeInstance p0);
    
    public abstract void removeChild(final MigratingCompensationEventSubscriptionInstance p0);
    
    public abstract void addChild(final MigratingCompensationEventSubscriptionInstance p0);
    
    public abstract boolean migrates();
    
    public abstract void detachChildren();
    
    public abstract void remove(final boolean p0, final boolean p1);
    
    public abstract Collection<MigratingProcessElementInstance> getChildren();
    
    public abstract Collection<MigratingScopeInstance> getChildScopeInstances();
    
    public abstract void removeUnmappedDependentInstances();
}
