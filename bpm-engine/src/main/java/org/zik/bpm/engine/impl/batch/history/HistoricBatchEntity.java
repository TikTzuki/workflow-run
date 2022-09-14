// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.history;

import org.zik.bpm.engine.impl.persistence.entity.HistoricJobLogManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricIncidentManager;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.batch.history.HistoricBatch;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;

public class HistoricBatchEntity extends HistoryEvent implements HistoricBatch, DbEntity
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String type;
    protected int totalJobs;
    protected int batchJobsPerSeed;
    protected int invocationsPerBatchJob;
    protected String seedJobDefinitionId;
    protected String monitorJobDefinitionId;
    protected String batchJobDefinitionId;
    protected String tenantId;
    protected String createUserId;
    protected Date startTime;
    protected Date endTime;
    
    @Override
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public int getTotalJobs() {
        return this.totalJobs;
    }
    
    public void setTotalJobs(final int totalJobs) {
        this.totalJobs = totalJobs;
    }
    
    @Override
    public int getBatchJobsPerSeed() {
        return this.batchJobsPerSeed;
    }
    
    public void setBatchJobsPerSeed(final int batchJobsPerSeed) {
        this.batchJobsPerSeed = batchJobsPerSeed;
    }
    
    @Override
    public int getInvocationsPerBatchJob() {
        return this.invocationsPerBatchJob;
    }
    
    public void setInvocationsPerBatchJob(final int invocationsPerBatchJob) {
        this.invocationsPerBatchJob = invocationsPerBatchJob;
    }
    
    @Override
    public String getSeedJobDefinitionId() {
        return this.seedJobDefinitionId;
    }
    
    public void setSeedJobDefinitionId(final String seedJobDefinitionId) {
        this.seedJobDefinitionId = seedJobDefinitionId;
    }
    
    @Override
    public String getMonitorJobDefinitionId() {
        return this.monitorJobDefinitionId;
    }
    
    public void setMonitorJobDefinitionId(final String monitorJobDefinitionId) {
        this.monitorJobDefinitionId = monitorJobDefinitionId;
    }
    
    @Override
    public String getBatchJobDefinitionId() {
        return this.batchJobDefinitionId;
    }
    
    public void setBatchJobDefinitionId(final String batchJobDefinitionId) {
        this.batchJobDefinitionId = batchJobDefinitionId;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getCreateUserId() {
        return this.createUserId;
    }
    
    public void setCreateUserId(final String createUserId) {
        this.createUserId = createUserId;
    }
    
    @Override
    public Date getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }
    
    @Override
    public Date getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("endTime", this.endTime);
        return persistentState;
    }
    
    public void delete() {
        final HistoricIncidentManager historicIncidentManager = Context.getCommandContext().getHistoricIncidentManager();
        historicIncidentManager.deleteHistoricIncidentsByJobDefinitionId(this.seedJobDefinitionId);
        historicIncidentManager.deleteHistoricIncidentsByJobDefinitionId(this.monitorJobDefinitionId);
        historicIncidentManager.deleteHistoricIncidentsByJobDefinitionId(this.batchJobDefinitionId);
        final HistoricJobLogManager historicJobLogManager = Context.getCommandContext().getHistoricJobLogManager();
        historicJobLogManager.deleteHistoricJobLogsByJobDefinitionId(this.seedJobDefinitionId);
        historicJobLogManager.deleteHistoricJobLogsByJobDefinitionId(this.monitorJobDefinitionId);
        historicJobLogManager.deleteHistoricJobLogsByJobDefinitionId(this.batchJobDefinitionId);
        Context.getCommandContext().getHistoricBatchManager().delete(this);
    }
}
