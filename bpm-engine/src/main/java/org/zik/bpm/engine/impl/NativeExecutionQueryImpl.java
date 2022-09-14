// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.runtime.Execution;
import org.zik.bpm.engine.runtime.NativeExecutionQuery;

public class NativeExecutionQueryImpl extends AbstractNativeQuery<NativeExecutionQuery, Execution> implements NativeExecutionQuery
{
    private static final long serialVersionUID = 1L;
    
    public NativeExecutionQueryImpl(final CommandContext commandContext) {
        super(commandContext);
    }
    
    public NativeExecutionQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public List<Execution> executeList(final CommandContext commandContext, final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return commandContext.getExecutionManager().findExecutionsByNativeQuery(parameterMap, firstResult, maxResults);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext, final Map<String, Object> parameterMap) {
        return commandContext.getExecutionManager().findExecutionCountByNativeQuery(parameterMap);
    }
}
