// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata.spi;

import java.util.Map;

public interface JobAcquisitionXml
{
    public static final String LOCK_TIME_IN_MILLIS = "lockTimeInMillis";
    public static final String WAIT_TIME_IN_MILLIS = "waitTimeInMillis";
    public static final String MAX_JOBS_PER_ACQUISITION = "maxJobsPerAcquisition";
    
    String getName();
    
    String getJobExecutorClassName();
    
    Map<String, String> getProperties();
}
