// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.history.NativeHistoricProcessInstanceQuery;

public class NativeHistoricProcessInstanceQueryImpl extends AbstractNativeQuery<NativeHistoricProcessInstanceQuery, HistoricProcessInstance> implements NativeHistoricProcessInstanceQuery
{
    private static final long serialVersionUID = 1L;
    
    public NativeHistoricProcessInstanceQueryImpl(final CommandContext commandContext) {
        super(commandContext);
    }
    
    public NativeHistoricProcessInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public List<HistoricProcessInstance> executeList(final CommandContext commandContext, final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return commandContext.getHistoricProcessInstanceManager().findHistoricProcessInstancesByNativeQuery(parameterMap, firstResult, maxResults);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext, final Map<String, Object> parameterMap) {
        return commandContext.getHistoricProcessInstanceManager().findHistoricProcessInstanceCountByNativeQuery(parameterMap);
    }
}
