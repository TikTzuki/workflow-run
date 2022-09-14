// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata.spi;

import java.util.Map;

public interface ProcessEnginePluginXml
{
    String getPluginClass();
    
    Map<String, String> getProperties();
}
