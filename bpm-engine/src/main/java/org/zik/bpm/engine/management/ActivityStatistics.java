// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import java.util.List;

public interface ActivityStatistics
{
    String getId();
    
    int getInstances();
    
    int getFailedJobs();
    
    List<IncidentStatistics> getIncidentStatistics();
}
