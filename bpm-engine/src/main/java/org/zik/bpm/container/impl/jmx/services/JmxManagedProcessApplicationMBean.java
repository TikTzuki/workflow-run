// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

import java.util.List;

public interface JmxManagedProcessApplicationMBean
{
    String getProcessApplicationName();
    
    List<String> getDeploymentIds();
    
    List<String> getDeploymentNames();
}
