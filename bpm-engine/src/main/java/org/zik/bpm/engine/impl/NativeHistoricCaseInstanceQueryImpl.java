// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.history.HistoricCaseInstance;
import org.zik.bpm.engine.history.NativeHistoricCaseInstanceQuery;

public class NativeHistoricCaseInstanceQueryImpl extends AbstractNativeQuery<NativeHistoricCaseInstanceQuery, HistoricCaseInstance> implements NativeHistoricCaseInstanceQuery
{
    private static final long serialVersionUID = 1L;
    
    public NativeHistoricCaseInstanceQueryImpl(final CommandContext commandContext) {
        super(commandContext);
    }
    
    public NativeHistoricCaseInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public List<HistoricCaseInstance> executeList(final CommandContext commandContext, final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return commandContext.getHistoricCaseInstanceManager().findHistoricCaseInstancesByNativeQuery(parameterMap, firstResult, maxResults);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext, final Map<String, Object> parameterMap) {
        return commandContext.getHistoricCaseInstanceManager().findHistoricCaseInstanceCountByNativeQuery(parameterMap);
    }
}
