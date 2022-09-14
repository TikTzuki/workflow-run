// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface TransitionInstance extends ProcessElementInstance
{
    @Deprecated
    String getTargetActivityId();
    
    String getActivityId();
    
    String getExecutionId();
    
    String getActivityType();
    
    String getActivityName();
    
    String[] getIncidentIds();
    
    Incident[] getIncidents();
}
