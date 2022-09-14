// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.externaltask;

import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;

public interface UpdateExternalTaskRetriesSelectBuilder
{
    UpdateExternalTaskRetriesBuilder externalTaskIds(final List<String> p0);
    
    UpdateExternalTaskRetriesBuilder externalTaskIds(final String... p0);
    
    UpdateExternalTaskRetriesBuilder processInstanceIds(final List<String> p0);
    
    UpdateExternalTaskRetriesBuilder processInstanceIds(final String... p0);
    
    UpdateExternalTaskRetriesBuilder externalTaskQuery(final ExternalTaskQuery p0);
    
    UpdateExternalTaskRetriesBuilder processInstanceQuery(final ProcessInstanceQuery p0);
    
    UpdateExternalTaskRetriesBuilder historicProcessInstanceQuery(final HistoricProcessInstanceQuery p0);
}
