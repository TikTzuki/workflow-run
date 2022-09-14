// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.optimize.OptimizeHistoricDecisionInstanceQueryCmd;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeOpenHistoricIncidentsQueryCmd;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeCompletedHistoricIncidentsQueryCmd;
import org.zik.bpm.engine.impl.persistence.entity.HistoricIncidentEntity;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeHistoricVariableUpdateQueryCmd;
import org.zik.bpm.engine.history.HistoricVariableUpdate;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeRunningHistoricProcessInstanceQueryCmd;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeCompletedHistoricProcessInstanceQueryCmd;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeHistoricIdentityLinkLogQueryCmd;
import org.zik.bpm.engine.impl.persistence.entity.optimize.OptimizeHistoricIdentityLinkLogEntity;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeHistoricUserOperationsLogQueryCmd;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeRunningHistoricTaskInstanceQueryCmd;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeCompletedHistoricTaskInstanceQueryCmd;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeRunningHistoricActivityInstanceQueryCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.optimize.OptimizeCompletedHistoricActivityInstanceQueryCmd;
import org.zik.bpm.engine.history.HistoricActivityInstance;
import java.util.List;
import java.util.Date;

public class OptimizeService extends ServiceImpl
{
    public List<HistoricActivityInstance> getCompletedHistoricActivityInstances(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricActivityInstance>>)new OptimizeCompletedHistoricActivityInstanceQueryCmd(finishedAfter, finishedAt, maxResults));
    }
    
    public List<HistoricActivityInstance> getRunningHistoricActivityInstances(final Date startedAfter, final Date startedAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricActivityInstance>>)new OptimizeRunningHistoricActivityInstanceQueryCmd(startedAfter, startedAt, maxResults));
    }
    
    public List<HistoricTaskInstance> getCompletedHistoricTaskInstances(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricTaskInstance>>)new OptimizeCompletedHistoricTaskInstanceQueryCmd(finishedAfter, finishedAt, maxResults));
    }
    
    public List<HistoricTaskInstance> getRunningHistoricTaskInstances(final Date startedAfter, final Date startedAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricTaskInstance>>)new OptimizeRunningHistoricTaskInstanceQueryCmd(startedAfter, startedAt, maxResults));
    }
    
    public List<UserOperationLogEntry> getHistoricUserOperationLogs(final Date occurredAfter, final Date occurredAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<UserOperationLogEntry>>)new OptimizeHistoricUserOperationsLogQueryCmd(occurredAfter, occurredAt, maxResults));
    }
    
    public List<OptimizeHistoricIdentityLinkLogEntity> getHistoricIdentityLinkLogs(final Date occurredAfter, final Date occurredAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<OptimizeHistoricIdentityLinkLogEntity>>)new OptimizeHistoricIdentityLinkLogQueryCmd(occurredAfter, occurredAt, maxResults));
    }
    
    public List<HistoricProcessInstance> getCompletedHistoricProcessInstances(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricProcessInstance>>)new OptimizeCompletedHistoricProcessInstanceQueryCmd(finishedAfter, finishedAt, maxResults));
    }
    
    public List<HistoricProcessInstance> getRunningHistoricProcessInstances(final Date startedAfter, final Date startedAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricProcessInstance>>)new OptimizeRunningHistoricProcessInstanceQueryCmd(startedAfter, startedAt, maxResults));
    }
    
    public List<HistoricVariableUpdate> getHistoricVariableUpdates(final Date occurredAfter, final Date occurredAt, final boolean excludeObjectValues, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricVariableUpdate>>)new OptimizeHistoricVariableUpdateQueryCmd(occurredAfter, occurredAt, excludeObjectValues, maxResults));
    }
    
    public List<HistoricIncidentEntity> getCompletedHistoricIncidents(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricIncidentEntity>>)new OptimizeCompletedHistoricIncidentsQueryCmd(finishedAfter, finishedAt, maxResults));
    }
    
    public List<HistoricIncidentEntity> getOpenHistoricIncidents(final Date createdAfter, final Date createdAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricIncidentEntity>>)new OptimizeOpenHistoricIncidentsQueryCmd(createdAfter, createdAt, maxResults));
    }
    
    public List<HistoricDecisionInstance> getHistoricDecisionInstances(final Date evaluatedAfter, final Date evaluatedAt, final int maxResults) {
        return this.commandExecutor.execute((Command<List<HistoricDecisionInstance>>)new OptimizeHistoricDecisionInstanceQueryCmd(evaluatedAfter, evaluatedAt, maxResults));
    }
}
