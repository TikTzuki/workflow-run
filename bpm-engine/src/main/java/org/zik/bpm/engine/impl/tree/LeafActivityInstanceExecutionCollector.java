// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class LeafActivityInstanceExecutionCollector implements TreeVisitor<PvmExecutionImpl>
{
    protected List<PvmExecutionImpl> leaves;
    
    public LeafActivityInstanceExecutionCollector() {
        this.leaves = new ArrayList<PvmExecutionImpl>();
    }
    
    @Override
    public void visit(final PvmExecutionImpl obj) {
        if (obj.getNonEventScopeExecutions().isEmpty() || (obj.getActivity() != null && !LegacyBehavior.hasInvalidIntermediaryActivityId(obj))) {
            this.leaves.add(obj);
        }
    }
    
    public List<PvmExecutionImpl> getLeaves() {
        return this.leaves;
    }
}
