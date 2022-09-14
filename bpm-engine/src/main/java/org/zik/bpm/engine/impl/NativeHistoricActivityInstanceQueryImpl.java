// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.history.HistoricActivityInstance;
import org.zik.bpm.engine.history.NativeHistoricActivityInstanceQuery;

public class NativeHistoricActivityInstanceQueryImpl extends AbstractNativeQuery<NativeHistoricActivityInstanceQuery, HistoricActivityInstance> implements NativeHistoricActivityInstanceQuery
{
    private static final long serialVersionUID = 1L;
    
    public NativeHistoricActivityInstanceQueryImpl(final CommandContext commandContext) {
        super(commandContext);
    }
    
    public NativeHistoricActivityInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public List<HistoricActivityInstance> executeList(final CommandContext commandContext, final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return commandContext.getHistoricActivityInstanceManager().findHistoricActivityInstancesByNativeQuery(parameterMap, firstResult, maxResults);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext, final Map<String, Object> parameterMap) {
        return commandContext.getHistoricActivityInstanceManager().findHistoricActivityInstanceCountByNativeQuery(parameterMap);
    }
}
