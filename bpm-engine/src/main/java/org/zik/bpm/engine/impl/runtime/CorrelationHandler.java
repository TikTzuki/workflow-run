// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public interface CorrelationHandler
{
    CorrelationHandlerResult correlateMessage(final CommandContext p0, final String p1, final CorrelationSet p2);
    
    List<CorrelationHandlerResult> correlateMessages(final CommandContext p0, final String p1, final CorrelationSet p2);
    
    List<CorrelationHandlerResult> correlateStartMessages(final CommandContext p0, final String p1, final CorrelationSet p2);
}
