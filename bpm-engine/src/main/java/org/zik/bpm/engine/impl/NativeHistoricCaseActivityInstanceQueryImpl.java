// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.history.HistoricCaseActivityInstance;
import org.zik.bpm.engine.history.NativeHistoricCaseActivityInstanceQuery;

public class NativeHistoricCaseActivityInstanceQueryImpl extends AbstractNativeQuery<NativeHistoricCaseActivityInstanceQuery, HistoricCaseActivityInstance> implements NativeHistoricCaseActivityInstanceQuery
{
    private static final long serialVersionUID = 1L;
    
    public NativeHistoricCaseActivityInstanceQueryImpl(final CommandContext commandContext) {
        super(commandContext);
    }
    
    public NativeHistoricCaseActivityInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public List<HistoricCaseActivityInstance> executeList(final CommandContext commandContext, final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return commandContext.getHistoricCaseActivityInstanceManager().findHistoricCaseActivityInstancesByNativeQuery(parameterMap, firstResult, maxResults);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext, final Map<String, Object> parameterMap) {
        return commandContext.getHistoricCaseActivityInstanceManager().findHistoricCaseActivityInstanceCountByNativeQuery(parameterMap);
    }
}
