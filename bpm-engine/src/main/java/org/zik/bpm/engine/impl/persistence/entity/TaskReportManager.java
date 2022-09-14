// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.history.DurationReportResult;
import org.zik.bpm.engine.history.HistoricTaskInstanceReportResult;
import org.zik.bpm.engine.impl.HistoricTaskInstanceReportImpl;
import org.zik.bpm.engine.task.TaskCountByCandidateGroupResult;
import java.util.List;
import org.zik.bpm.engine.impl.TaskReportImpl;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class TaskReportManager extends AbstractManager
{
    public List<TaskCountByCandidateGroupResult> createTaskCountByCandidateGroupReport(final TaskReportImpl query) {
        this.configureQuery(query);
        return (List<TaskCountByCandidateGroupResult>)this.getDbEntityManager().selectListWithRawParameter("selectTaskCountByCandidateGroupReportQuery", query, 0, Integer.MAX_VALUE);
    }
    
    public List<HistoricTaskInstanceReportResult> selectHistoricTaskInstanceCountByTaskNameReport(final HistoricTaskInstanceReportImpl query) {
        this.configureQuery(query);
        return (List<HistoricTaskInstanceReportResult>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricTaskInstanceCountByTaskNameReport", query, 0, Integer.MAX_VALUE);
    }
    
    public List<HistoricTaskInstanceReportResult> selectHistoricTaskInstanceCountByProcDefKeyReport(final HistoricTaskInstanceReportImpl query) {
        this.configureQuery(query);
        return (List<HistoricTaskInstanceReportResult>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricTaskInstanceCountByProcDefKeyReport", query, 0, Integer.MAX_VALUE);
    }
    
    public List<DurationReportResult> createHistoricTaskDurationReport(final HistoricTaskInstanceReportImpl query) {
        this.configureQuery(query);
        return (List<DurationReportResult>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricTaskInstanceDurationReport", query, 0, Integer.MAX_VALUE);
    }
    
    protected void configureQuery(final HistoricTaskInstanceReportImpl parameter) {
        this.getAuthorizationManager().checkAuthorization(Permissions.READ_HISTORY, Resources.PROCESS_DEFINITION, "*");
        this.getTenantManager().configureTenantCheck(parameter.getTenantCheck());
    }
    
    protected void configureQuery(final TaskReportImpl parameter) {
        this.getAuthorizationManager().checkAuthorization(Permissions.READ, Resources.TASK, "*");
        this.getTenantManager().configureTenantCheck(parameter.getTenantCheck());
    }
}
