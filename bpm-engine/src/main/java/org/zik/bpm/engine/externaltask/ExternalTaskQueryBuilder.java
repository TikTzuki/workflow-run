// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.externaltask;

import java.util.List;

public interface ExternalTaskQueryBuilder
{
    ExternalTaskQueryTopicBuilder topic(final String p0, final long p1);
    
    List<LockedExternalTask> execute();
}
