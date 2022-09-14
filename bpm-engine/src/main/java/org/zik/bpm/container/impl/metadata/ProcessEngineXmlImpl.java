// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata;

import org.zik.bpm.container.impl.metadata.spi.ProcessEnginePluginXml;
import java.util.List;
import java.util.Map;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;

public class ProcessEngineXmlImpl implements ProcessEngineXml
{
    protected String name;
    protected boolean isDefault;
    protected String configurationClass;
    protected String jobAcquisitionName;
    protected String datasource;
    protected Map<String, String> properties;
    protected List<ProcessEnginePluginXml> plugins;
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public boolean isDefault() {
        return this.isDefault;
    }
    
    public void setDefault(final boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    @Override
    public String getConfigurationClass() {
        return this.configurationClass;
    }
    
    public void setConfigurationClass(final String configurationClass) {
        this.configurationClass = configurationClass;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
    
    @Override
    public String getDatasource() {
        return this.datasource;
    }
    
    public void setDatasource(final String datasource) {
        this.datasource = datasource;
    }
    
    @Override
    public String getJobAcquisitionName() {
        return this.jobAcquisitionName;
    }
    
    public void setJobAcquisitionName(final String jobAcquisitionName) {
        this.jobAcquisitionName = jobAcquisitionName;
    }
    
    @Override
    public List<ProcessEnginePluginXml> getPlugins() {
        return this.plugins;
    }
    
    public void setPlugins(final List<ProcessEnginePluginXml> plugins) {
        this.plugins = plugins;
    }
}
