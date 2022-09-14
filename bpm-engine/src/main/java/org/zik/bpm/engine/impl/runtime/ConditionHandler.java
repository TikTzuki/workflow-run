// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public interface ConditionHandler
{
    List<ConditionHandlerResult> evaluateStartCondition(final CommandContext p0, final ConditionSet p1);
}
