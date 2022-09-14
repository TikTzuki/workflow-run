// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;

public class FlowScopeWalker extends SingleReferenceWalker<ScopeImpl>
{
    public FlowScopeWalker(final ScopeImpl startActivity) {
        super(startActivity);
    }
    
    @Override
    protected ScopeImpl nextElement() {
        final ScopeImpl currentElement = this.getCurrentElement();
        if (currentElement != null && ActivityImpl.class.isAssignableFrom(currentElement.getClass())) {
            return ((PvmActivity)currentElement).getFlowScope();
        }
        return null;
    }
}
