// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.List;
import java.util.Date;
import org.zik.bpm.engine.query.Report;

public interface HistoricTaskInstanceReport extends Report
{
    HistoricTaskInstanceReport completedAfter(final Date p0);
    
    HistoricTaskInstanceReport completedBefore(final Date p0);
    
    List<HistoricTaskInstanceReportResult> countByProcessDefinitionKey();
    
    List<HistoricTaskInstanceReportResult> countByTaskName();
}
