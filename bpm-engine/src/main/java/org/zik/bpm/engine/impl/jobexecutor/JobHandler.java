// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public interface JobHandler<T extends JobHandlerConfiguration>
{
    String getType();
    
    void execute(final T p0, final ExecutionEntity p1, final CommandContext p2, final String p3);
    
    T newConfiguration(final String p0);
    
    void onDelete(final T p0, final JobEntity p1);
}
