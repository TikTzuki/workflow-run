// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.spi;

public enum ServiceTypes implements PlatformServiceContainer.ServiceType
{
    BPM_PLATFORM("org.camunda.bpm.platform"), 
    PROCESS_ENGINE("org.camunda.bpm.platform.process-engine"), 
    JOB_EXECUTOR("org.camunda.bpm.platform.job-executor"), 
    PROCESS_APPLICATION("org.camunda.bpm.platform.job-executor.process-application");
    
    protected String serviceRealm;
    
    private ServiceTypes(final String serviceRealm) {
        this.serviceRealm = serviceRealm;
    }
    
    @Override
    public String getTypeName() {
        return this.serviceRealm;
    }
}
