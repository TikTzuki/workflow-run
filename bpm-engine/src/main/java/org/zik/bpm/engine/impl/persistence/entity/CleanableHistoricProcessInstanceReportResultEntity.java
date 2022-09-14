// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.CleanableHistoricProcessInstanceReportResult;

public class CleanableHistoricProcessInstanceReportResultEntity implements CleanableHistoricProcessInstanceReportResult
{
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processDefinitionName;
    protected int processDefinitionVersion;
    protected Integer historyTimeToLive;
    protected long finishedProcessInstanceCount;
    protected long cleanableProcessInstanceCount;
    protected String tenantId;
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    @Override
    public String getProcessDefinitionName() {
        return this.processDefinitionName;
    }
    
    public void setProcessDefinitionName(final String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }
    
    @Override
    public int getProcessDefinitionVersion() {
        return this.processDefinitionVersion;
    }
    
    public void setProcessDefinitionVersion(final int processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }
    
    @Override
    public Integer getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }
    
    public void setHistoryTimeToLive(final Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
    
    @Override
    public long getFinishedProcessInstanceCount() {
        return this.finishedProcessInstanceCount;
    }
    
    public void setFinishedProcessInstanceCount(final Long finishedProcessInstanceCount) {
        this.finishedProcessInstanceCount = finishedProcessInstanceCount;
    }
    
    @Override
    public long getCleanableProcessInstanceCount() {
        return this.cleanableProcessInstanceCount;
    }
    
    public void setCleanableProcessInstanceCount(final Long cleanableProcessInstanceCount) {
        this.cleanableProcessInstanceCount = cleanableProcessInstanceCount;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[processDefinitionId = " + this.processDefinitionId + ", processDefinitionKey = " + this.processDefinitionKey + ", processDefinitionName = " + this.processDefinitionName + ", processDefinitionVersion = " + this.processDefinitionVersion + ", historyTimeToLive = " + this.historyTimeToLive + ", finishedProcessInstanceCount = " + this.finishedProcessInstanceCount + ", cleanableProcessInstanceCount = " + this.cleanableProcessInstanceCount + ", tenantId = " + this.tenantId + "]";
    }
}
