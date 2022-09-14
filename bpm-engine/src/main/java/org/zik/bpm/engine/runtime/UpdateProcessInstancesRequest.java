// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import java.util.List;

public interface UpdateProcessInstancesRequest
{
    UpdateProcessInstancesSuspensionStateBuilder byProcessInstanceIds(final List<String> p0);
    
    UpdateProcessInstancesSuspensionStateBuilder byProcessInstanceIds(final String... p0);
    
    UpdateProcessInstancesSuspensionStateBuilder byProcessInstanceQuery(final ProcessInstanceQuery p0);
    
    UpdateProcessInstancesSuspensionStateBuilder byHistoricProcessInstanceQuery(final HistoricProcessInstanceQuery p0);
}
