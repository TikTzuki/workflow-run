// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import org.zik.bpm.engine.history.NativeHistoricDecisionInstanceQuery;

public class NativeHistoryDecisionInstanceQueryImpl extends AbstractNativeQuery<NativeHistoricDecisionInstanceQuery, HistoricDecisionInstance> implements NativeHistoricDecisionInstanceQuery
{
    private static final long serialVersionUID = 1L;
    
    public NativeHistoryDecisionInstanceQueryImpl(final CommandContext commandContext) {
        super(commandContext);
    }
    
    public NativeHistoryDecisionInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext, final Map<String, Object> parameterMap) {
        return commandContext.getHistoricDecisionInstanceManager().findHistoricDecisionInstanceCountByNativeQuery(parameterMap);
    }
    
    @Override
    public List<HistoricDecisionInstance> executeList(final CommandContext commandContext, final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return commandContext.getHistoricDecisionInstanceManager().findHistoricDecisionInstancesByNativeQuery(parameterMap, firstResult, maxResults);
    }
}
