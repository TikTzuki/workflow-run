// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata;

import java.util.Map;
import org.zik.bpm.container.impl.metadata.spi.JobAcquisitionXml;

public class JobAcquisitionXmlImpl implements JobAcquisitionXml
{
    private String name;
    private String jobExecutorClassName;
    private Map<String, String> properties;
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
    
    @Override
    public String getJobExecutorClassName() {
        return this.jobExecutorClassName;
    }
    
    public void setJobExecutorClassName(final String jobExecutorClassName) {
        this.jobExecutorClassName = jobExecutorClassName;
    }
}
