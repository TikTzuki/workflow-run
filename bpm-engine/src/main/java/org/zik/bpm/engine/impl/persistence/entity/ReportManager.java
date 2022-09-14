// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.DurationReportResult;
import java.util.List;
import org.zik.bpm.engine.impl.HistoricProcessInstanceReportImpl;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class ReportManager extends AbstractManager
{
    public List<DurationReportResult> selectHistoricProcessInstanceDurationReport(final HistoricProcessInstanceReportImpl query) {
        this.configureQuery(query);
        return (List<DurationReportResult>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricProcessInstanceDurationReport", query, 0, Integer.MAX_VALUE);
    }
    
    protected void configureQuery(final HistoricProcessInstanceReportImpl parameter) {
        this.getTenantManager().configureTenantCheck(parameter.getTenantCheck());
    }
}
