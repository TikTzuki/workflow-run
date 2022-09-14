// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.event;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;

public interface EventHandler
{
    String getEventHandlerType();
    
    void handleEvent(final EventSubscriptionEntity p0, final Object p1, final Object p2, final String p3, final CommandContext p4);
}
