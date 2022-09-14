// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import java.util.HashMap;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Map;

public class MigratingScopeInstanceBranch
{
    protected Map<ScopeImpl, MigratingScopeInstance> scopeInstances;
    
    public MigratingScopeInstanceBranch() {
        this(new HashMap<ScopeImpl, MigratingScopeInstance>());
    }
    
    protected MigratingScopeInstanceBranch(final Map<ScopeImpl, MigratingScopeInstance> scopeInstances) {
        this.scopeInstances = scopeInstances;
    }
    
    public MigratingScopeInstanceBranch copy() {
        return new MigratingScopeInstanceBranch(new HashMap<ScopeImpl, MigratingScopeInstance>(this.scopeInstances));
    }
    
    public MigratingScopeInstance getInstance(final ScopeImpl scope) {
        return this.scopeInstances.get(scope);
    }
    
    public boolean hasInstance(final ScopeImpl scope) {
        return this.scopeInstances.containsKey(scope);
    }
    
    public void visited(final MigratingScopeInstance scopeInstance) {
        final ScopeImpl targetScope = scopeInstance.getTargetScope();
        if (targetScope.isScope()) {
            this.scopeInstances.put(targetScope, scopeInstance);
        }
    }
}
