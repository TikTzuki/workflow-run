// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class ExecutionWalker extends SingleReferenceWalker<PvmExecutionImpl>
{
    public ExecutionWalker(final PvmExecutionImpl initialElement) {
        super(initialElement);
    }
    
    @Override
    protected PvmExecutionImpl nextElement() {
        return this.getCurrentElement().getParent();
    }
}
