// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.batch.Batch;

public interface UpdateProcessInstancesSuspensionStateBuilder extends UpdateProcessInstanceSuspensionStateBuilder, UpdateProcessInstancesRequest
{
    Batch activateAsync();
    
    Batch suspendAsync();
}
