// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata.spi;

import java.util.List;
import java.util.Map;

public interface ProcessEngineXml
{
    String getName();
    
    boolean isDefault();
    
    String getConfigurationClass();
    
    String getDatasource();
    
    Map<String, String> getProperties();
    
    String getJobAcquisitionName();
    
    List<ProcessEnginePluginXml> getPlugins();
}
