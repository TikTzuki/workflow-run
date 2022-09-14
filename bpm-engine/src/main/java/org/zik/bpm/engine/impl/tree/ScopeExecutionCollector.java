// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class ScopeExecutionCollector implements TreeVisitor<PvmExecutionImpl>
{
    protected List<PvmExecutionImpl> scopeExecutions;
    
    public ScopeExecutionCollector() {
        this.scopeExecutions = new ArrayList<PvmExecutionImpl>();
    }
    
    @Override
    public void visit(final PvmExecutionImpl obj) {
        if (obj.isScope()) {
            this.scopeExecutions.add(obj);
        }
    }
    
    public List<PvmExecutionImpl> getScopeExecutions() {
        return this.scopeExecutions;
    }
}
