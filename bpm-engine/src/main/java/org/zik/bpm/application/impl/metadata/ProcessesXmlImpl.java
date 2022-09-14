// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl.metadata;

import org.zik.bpm.application.impl.metadata.spi.ProcessArchiveXml;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import java.util.List;
import org.zik.bpm.application.impl.metadata.spi.ProcessesXml;

public class ProcessesXmlImpl implements ProcessesXml
{
    private List<ProcessEngineXml> processEngineXmls;
    private List<ProcessArchiveXml> processArchiveXmls;
    
    public ProcessesXmlImpl(final List<ProcessEngineXml> processEngineXmls, final List<ProcessArchiveXml> processArchiveXmls) {
        this.processEngineXmls = processEngineXmls;
        this.processArchiveXmls = processArchiveXmls;
    }
    
    @Override
    public List<ProcessEngineXml> getProcessEngines() {
        return this.processEngineXmls;
    }
    
    @Override
    public List<ProcessArchiveXml> getProcessArchives() {
        return this.processArchiveXmls;
    }
}
