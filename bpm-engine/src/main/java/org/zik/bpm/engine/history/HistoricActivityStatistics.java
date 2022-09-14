// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface HistoricActivityStatistics
{
    String getId();
    
    long getInstances();
    
    long getFinished();
    
    long getCanceled();
    
    long getCompleteScope();
    
    long getOpenIncidents();
    
    long getResolvedIncidents();
    
    long getDeletedIncidents();
}
