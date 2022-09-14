// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface ActivityInstance extends ProcessElementInstance
{
    String getActivityId();
    
    String getActivityName();
    
    String getActivityType();
    
    ActivityInstance[] getChildActivityInstances();
    
    TransitionInstance[] getChildTransitionInstances();
    
    String[] getExecutionIds();
    
    ActivityInstance[] getActivityInstances(final String p0);
    
    TransitionInstance[] getTransitionInstances(final String p0);
    
    String[] getIncidentIds();
    
    Incident[] getIncidents();
}
