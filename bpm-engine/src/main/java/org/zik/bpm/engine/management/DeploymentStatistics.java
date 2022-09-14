// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import java.util.List;
import org.zik.bpm.engine.repository.Deployment;

public interface DeploymentStatistics extends Deployment
{
    int getInstances();
    
    int getFailedJobs();
    
    List<IncidentStatistics> getIncidentStatistics();
}
