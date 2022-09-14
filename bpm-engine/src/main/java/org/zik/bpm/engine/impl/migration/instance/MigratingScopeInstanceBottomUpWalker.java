// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.tree.SingleReferenceWalker;

public class MigratingScopeInstanceBottomUpWalker extends SingleReferenceWalker<MigratingScopeInstance>
{
    protected MigratingScopeInstance parent;
    
    public MigratingScopeInstanceBottomUpWalker(final MigratingScopeInstance initialElement) {
        super(initialElement);
        this.parent = null;
        this.parent = initialElement.getParent();
    }
    
    @Override
    protected MigratingScopeInstance nextElement() {
        final MigratingScopeInstance nextElement = this.parent;
        if (this.parent != null) {
            this.parent = this.parent.getParent();
        }
        return nextElement;
    }
}
