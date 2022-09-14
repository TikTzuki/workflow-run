// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata;

import java.util.Map;
import org.zik.bpm.container.impl.metadata.spi.JobAcquisitionXml;
import java.util.List;
import org.zik.bpm.container.impl.metadata.spi.JobExecutorXml;

public class JobExecutorXmlImpl implements JobExecutorXml
{
    protected List<JobAcquisitionXml> jobAcquisitions;
    protected String jobExecutorClass;
    protected Map<String, String> properties;
    
    @Override
    public List<JobAcquisitionXml> getJobAcquisitions() {
        return this.jobAcquisitions;
    }
    
    public void setJobAcquisitions(final List<JobAcquisitionXml> jobAcquisitions) {
        this.jobAcquisitions = jobAcquisitions;
    }
    
    public String getJobExecutorClass() {
        return this.jobExecutorClass;
    }
    
    public void setJobExecutorClass(final String jobExecutorClass) {
        this.jobExecutorClass = jobExecutorClass;
    }
    
    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.properties;
    }
}
