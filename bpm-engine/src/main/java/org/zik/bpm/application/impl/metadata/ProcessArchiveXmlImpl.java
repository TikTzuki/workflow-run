// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl.metadata;

import java.util.Map;
import java.util.List;
import org.zik.bpm.application.impl.metadata.spi.ProcessArchiveXml;

public class ProcessArchiveXmlImpl implements ProcessArchiveXml
{
    private String name;
    private String tenantId;
    private String processEngineName;
    private List<String> processResourceNames;
    private Map<String, String> properties;
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getProcessEngineName() {
        return this.processEngineName;
    }
    
    public void setProcessEngineName(final String processEngineName) {
        this.processEngineName = processEngineName;
    }
    
    @Override
    public List<String> getProcessResourceNames() {
        return this.processResourceNames;
    }
    
    public void setProcessResourceNames(final List<String> processResourceNames) {
        this.processResourceNames = processResourceNames;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
}
