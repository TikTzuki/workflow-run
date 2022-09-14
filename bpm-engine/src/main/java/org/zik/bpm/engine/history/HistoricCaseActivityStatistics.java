// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface HistoricCaseActivityStatistics
{
    String getId();
    
    long getAvailable();
    
    long getEnabled();
    
    long getDisabled();
    
    long getActive();
    
    long getCompleted();
    
    long getTerminated();
}
