// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public interface PriorityProvider<T>
{
    long determinePriority(final ExecutionEntity p0, final T p1, final String p2);
}
