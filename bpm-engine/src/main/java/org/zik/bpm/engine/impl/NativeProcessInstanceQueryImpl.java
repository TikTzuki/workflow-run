// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.runtime.NativeProcessInstanceQuery;

public class NativeProcessInstanceQueryImpl extends AbstractNativeQuery<NativeProcessInstanceQuery, ProcessInstance> implements NativeProcessInstanceQuery
{
    private static final long serialVersionUID = 1L;
    
    public NativeProcessInstanceQueryImpl(final CommandContext commandContext) {
        super(commandContext);
    }
    
    public NativeProcessInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public List<ProcessInstance> executeList(final CommandContext commandContext, final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return commandContext.getExecutionManager().findProcessInstanceByNativeQuery(parameterMap, firstResult, maxResults);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext, final Map<String, Object> parameterMap) {
        return commandContext.getExecutionManager().findExecutionCountByNativeQuery(parameterMap);
    }
}
