// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import org.zik.bpm.application.ProcessApplicationRegistration;

public interface ProcessApplicationDeployment extends DeploymentWithDefinitions
{
    public static final String PROCESS_APPLICATION_DEPLOYMENT_SOURCE = "process application";
    
    ProcessApplicationRegistration getProcessApplicationRegistration();
}
