// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.delegate;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.core.delegate.CoreActivityBehavior;

public interface ActivityBehavior extends CoreActivityBehavior<ActivityExecution>
{
    void execute(final ActivityExecution p0) throws Exception;
}
