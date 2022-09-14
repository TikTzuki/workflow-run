// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import java.util.Arrays;
import java.util.Collection;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.impl.tree.ReferenceWalker;

public class ActivityInstanceWalker extends ReferenceWalker<ActivityInstance>
{
    public ActivityInstanceWalker(final ActivityInstance initialElement) {
        super(initialElement);
    }
    
    @Override
    protected Collection<ActivityInstance> nextElements() {
        final ActivityInstance[] children = this.getCurrentElement().getChildActivityInstances();
        return Arrays.asList(children);
    }
}
