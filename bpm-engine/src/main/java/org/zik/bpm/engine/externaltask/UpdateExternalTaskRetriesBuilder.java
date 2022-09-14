// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.externaltask;

import org.zik.bpm.engine.batch.Batch;

public interface UpdateExternalTaskRetriesBuilder extends UpdateExternalTaskRetriesSelectBuilder
{
    void set(final int p0);
    
    Batch setAsync(final int p0);
}
