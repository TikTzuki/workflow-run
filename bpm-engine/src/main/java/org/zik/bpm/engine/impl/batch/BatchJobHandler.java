// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.jobexecutor.JobHandler;

public interface BatchJobHandler<T> extends JobHandler<BatchJobConfiguration>
{
    byte[] writeConfiguration(final T p0);
    
    T readConfiguration(final byte[] p0);
    
    JobDeclaration<?, MessageEntity> getJobDeclaration();
    
    boolean createJobs(final BatchEntity p0);
    
    void deleteJobs(final BatchEntity p0);
}
