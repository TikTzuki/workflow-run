// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ActivityStartBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;

public interface PvmActivity extends PvmScope
{
    ActivityBehavior getActivityBehavior();
    
    ActivityStartBehavior getActivityStartBehavior();
    
    PvmTransition findOutgoingTransition(final String p0);
    
    List<PvmTransition> getOutgoingTransitions();
    
    List<PvmTransition> getIncomingTransitions();
    
    boolean isAsyncBefore();
    
    boolean isAsyncAfter();
}
