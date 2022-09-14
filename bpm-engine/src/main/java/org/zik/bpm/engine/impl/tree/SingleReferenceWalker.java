// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import java.util.Collections;
import java.util.Collection;

public abstract class SingleReferenceWalker<T> extends ReferenceWalker<T>
{
    public SingleReferenceWalker(final T initialElement) {
        super(initialElement);
    }
    
    @Override
    protected Collection<T> nextElements() {
        final T nextElement = this.nextElement();
        if (nextElement != null) {
            return Collections.singleton(nextElement);
        }
        return (Collection<T>)Collections.emptyList();
    }
    
    protected abstract T nextElement();
}
