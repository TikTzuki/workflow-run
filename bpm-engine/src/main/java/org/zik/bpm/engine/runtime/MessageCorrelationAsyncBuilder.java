// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.batch.Batch;
import java.util.Map;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import java.util.List;

public interface MessageCorrelationAsyncBuilder
{
    MessageCorrelationAsyncBuilder processInstanceIds(final List<String> p0);
    
    MessageCorrelationAsyncBuilder processInstanceQuery(final ProcessInstanceQuery p0);
    
    MessageCorrelationAsyncBuilder historicProcessInstanceQuery(final HistoricProcessInstanceQuery p0);
    
    MessageCorrelationAsyncBuilder setVariable(final String p0, final Object p1);
    
    MessageCorrelationAsyncBuilder setVariables(final Map<String, Object> p0);
    
    Batch correlateAllAsync();
}
