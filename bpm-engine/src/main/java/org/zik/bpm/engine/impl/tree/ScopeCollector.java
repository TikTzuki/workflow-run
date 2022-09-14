// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;

public class ScopeCollector implements TreeVisitor<ScopeImpl>
{
    protected List<ScopeImpl> scopes;
    
    public ScopeCollector() {
        this.scopes = new ArrayList<ScopeImpl>();
    }
    
    @Override
    public void visit(final ScopeImpl obj) {
        if (obj != null && obj.isScope()) {
            this.scopes.add(obj);
        }
    }
    
    public List<ScopeImpl> getScopes() {
        return this.scopes;
    }
}
