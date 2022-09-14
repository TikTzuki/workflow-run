// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

public interface JobDefinition
{
    String getId();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionKey();
    
    String getJobType();
    
    String getJobConfiguration();
    
    String getActivityId();
    
    boolean isSuspended();
    
    Long getOverridingJobPriority();
    
    String getTenantId();
    
    String getDeploymentId();
}
