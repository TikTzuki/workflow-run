// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.spi;

import java.util.Set;
import java.util.List;

public interface PlatformServiceContainer
{
     <S> void startService(final ServiceType p0, final String p1, final PlatformService<S> p2);
    
     <S> void startService(final String p0, final PlatformService<S> p1);
    
    void stopService(final ServiceType p0, final String p1);
    
    void stopService(final String p0);
    
    DeploymentOperation.DeploymentOperationBuilder createDeploymentOperation(final String p0);
    
    DeploymentOperation.DeploymentOperationBuilder createUndeploymentOperation(final String p0);
    
     <S> S getService(final ServiceType p0, final String p1);
    
     <S> S getServiceValue(final ServiceType p0, final String p1);
    
     <S> List<PlatformService<S>> getServicesByType(final ServiceType p0);
    
    Set<String> getServiceNames(final ServiceType p0);
    
     <S> List<S> getServiceValuesByType(final ServiceType p0);
    
    void executeDeploymentOperation(final DeploymentOperation p0);
    
    public interface ServiceType
    {
        String getTypeName();
    }
}
