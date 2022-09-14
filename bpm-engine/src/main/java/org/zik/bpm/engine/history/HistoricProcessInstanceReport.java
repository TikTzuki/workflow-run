// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Report;

public interface HistoricProcessInstanceReport extends Report
{
    HistoricProcessInstanceReport startedBefore(final Date p0);
    
    HistoricProcessInstanceReport startedAfter(final Date p0);
    
    HistoricProcessInstanceReport processDefinitionIdIn(final String... p0);
    
    HistoricProcessInstanceReport processDefinitionKeyIn(final String... p0);
}
