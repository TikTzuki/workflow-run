// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata;

import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import java.util.List;
import org.zik.bpm.container.impl.metadata.spi.JobExecutorXml;
import org.zik.bpm.container.impl.metadata.spi.BpmPlatformXml;

public class BpmPlatformXmlImpl implements BpmPlatformXml
{
    protected JobExecutorXml jobExecutor;
    protected List<ProcessEngineXml> processEngines;
    
    public BpmPlatformXmlImpl(final JobExecutorXml jobExecutor, final List<ProcessEngineXml> processEngines) {
        this.jobExecutor = jobExecutor;
        this.processEngines = processEngines;
    }
    
    @Override
    public List<ProcessEngineXml> getProcessEngines() {
        return this.processEngines;
    }
    
    public void setProcessEngines(final List<ProcessEngineXml> processEngines) {
        this.processEngines = processEngines;
    }
    
    @Override
    public JobExecutorXml getJobExecutor() {
        return this.jobExecutor;
    }
    
    public void setJobExecutor(final JobExecutorXml jobExecutor) {
        this.jobExecutor = jobExecutor;
    }
}
