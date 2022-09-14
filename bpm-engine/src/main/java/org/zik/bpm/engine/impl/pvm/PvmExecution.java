// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

import java.util.Map;

public interface PvmExecution
{
    void signal(final String p0, final Object p1);
    
    PvmActivity getActivity();
    
    boolean hasVariable(final String p0);
    
    void setVariable(final String p0, final Object p1);
    
    Object getVariable(final String p0);
    
    Map<String, Object> getVariables();
}
