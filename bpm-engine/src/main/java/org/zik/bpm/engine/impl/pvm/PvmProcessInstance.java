// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

import java.util.List;
import java.util.Map;

public interface PvmProcessInstance extends PvmExecution
{
    void start();
    
    void start(final Map<String, Object> p0);
    
    PvmExecution findExecution(final String p0);
    
    List<PvmExecution> findExecutions(final String p0);
    
    List<String> findActiveActivityIds();
    
    boolean isEnded();
    
    void deleteCascade(final String p0);
}
