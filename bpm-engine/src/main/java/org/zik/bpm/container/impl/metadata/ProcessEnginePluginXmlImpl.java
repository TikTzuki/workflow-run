// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata;

import java.util.Map;
import org.zik.bpm.container.impl.metadata.spi.ProcessEnginePluginXml;

public class ProcessEnginePluginXmlImpl implements ProcessEnginePluginXml
{
    protected String pluginClass;
    protected Map<String, String> properties;
    
    @Override
    public String getPluginClass() {
        return this.pluginClass;
    }
    
    public void setPluginClass(final String pluginClass) {
        this.pluginClass = pluginClass;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
}
