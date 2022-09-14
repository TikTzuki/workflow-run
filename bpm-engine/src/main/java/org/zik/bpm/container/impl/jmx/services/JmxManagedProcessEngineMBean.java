// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

import java.util.Set;

public interface JmxManagedProcessEngineMBean
{
    String getName();
    
    Set<String> getRegisteredDeployments();
    
    void registerDeployment(final String p0);
    
    void unregisterDeployment(final String p0);
    
    void reportDbMetrics();
}
