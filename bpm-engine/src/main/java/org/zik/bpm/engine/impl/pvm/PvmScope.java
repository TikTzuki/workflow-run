// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;

public interface PvmScope extends PvmProcessElement
{
    boolean isScope();
    
    boolean isSubProcessScope();
    
    PvmScope getEventScope();
    
    ScopeImpl getFlowScope();
    
    PvmScope getLevelOfSubprocessScope();
    
    List<? extends PvmActivity> getActivities();
    
    PvmActivity findActivity(final String p0);
    
    PvmActivity findActivityAtLevelOfSubprocess(final String p0);
    
    TransitionImpl findTransition(final String p0);
}
