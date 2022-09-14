// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.delegate;

import java.util.List;

public interface ModificationObserverBehavior extends ActivityBehavior
{
    List<ActivityExecution> initializeScope(final ActivityExecution p0, final int p1);
    
    ActivityExecution createInnerInstance(final ActivityExecution p0);
    
    void destroyInnerInstance(final ActivityExecution p0);
}
