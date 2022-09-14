// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata.spi;

import java.util.List;

public interface BpmPlatformXml
{
    JobExecutorXml getJobExecutor();
    
    List<ProcessEngineXml> getProcessEngines();
}
