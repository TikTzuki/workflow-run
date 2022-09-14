// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata.spi;

import java.util.Map;
import java.util.List;

public interface JobExecutorXml
{
    public static final String QUEUE_SIZE = "queueSize";
    public static final String CORE_POOL_SIZE = "corePoolSize";
    public static final String MAX_POOL_SIZE = "maxPoolSize";
    public static final String KEEP_ALIVE_TIME = "keepAliveTime";
    
    List<JobAcquisitionXml> getJobAcquisitions();
    
    Map<String, String> getProperties();
}
