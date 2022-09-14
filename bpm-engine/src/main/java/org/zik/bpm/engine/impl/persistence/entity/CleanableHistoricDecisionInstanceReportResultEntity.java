// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.CleanableHistoricDecisionInstanceReportResult;

public class CleanableHistoricDecisionInstanceReportResultEntity implements CleanableHistoricDecisionInstanceReportResult
{
    protected String decisionDefinitionId;
    protected String decisionDefinitionKey;
    protected String decisionDefinitionName;
    protected int decisionDefinitionVersion;
    protected Integer historyTimeToLive;
    protected long finishedDecisionInstanceCount;
    protected long cleanableDecisionInstanceCount;
    protected String tenantId;
    
    @Override
    public String getDecisionDefinitionId() {
        return this.decisionDefinitionId;
    }
    
    public void setDecisionDefinitionId(final String decisionDefinitionId) {
        this.decisionDefinitionId = decisionDefinitionId;
    }
    
    @Override
    public String getDecisionDefinitionKey() {
        return this.decisionDefinitionKey;
    }
    
    public void setDecisionDefinitionKey(final String decisionDefinitionKey) {
        this.decisionDefinitionKey = decisionDefinitionKey;
    }
    
    @Override
    public String getDecisionDefinitionName() {
        return this.decisionDefinitionName;
    }
    
    public void setDecisionDefinitionName(final String decisionDefinitionName) {
        this.decisionDefinitionName = decisionDefinitionName;
    }
    
    @Override
    public int getDecisionDefinitionVersion() {
        return this.decisionDefinitionVersion;
    }
    
    public void setDecisionDefinitionVersion(final int decisionDefinitionVersion) {
        this.decisionDefinitionVersion = decisionDefinitionVersion;
    }
    
    @Override
    public Integer getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }
    
    public void setHistoryTimeToLive(final Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
    
    @Override
    public long getFinishedDecisionInstanceCount() {
        return this.finishedDecisionInstanceCount;
    }
    
    public void setFinishedDecisionInstanceCount(final long finishedDecisionInstanceCount) {
        this.finishedDecisionInstanceCount = finishedDecisionInstanceCount;
    }
    
    @Override
    public long getCleanableDecisionInstanceCount() {
        return this.cleanableDecisionInstanceCount;
    }
    
    public void setCleanableDecisionInstanceCount(final long cleanableDecisionInstanceCount) {
        this.cleanableDecisionInstanceCount = cleanableDecisionInstanceCount;
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
        return this.getClass().getSimpleName() + "[decisionDefinitionId = " + this.decisionDefinitionId + ", decisionDefinitionKey = " + this.decisionDefinitionKey + ", decisionDefinitionName = " + this.decisionDefinitionName + ", decisionDefinitionVersion = " + this.decisionDefinitionVersion + ", historyTimeToLive = " + this.historyTimeToLive + ", finishedDecisionInstanceCount = " + this.finishedDecisionInstanceCount + ", cleanableDecisionInstanceCount = " + this.cleanableDecisionInstanceCount + ", tenantId = " + this.tenantId + "]";
    }
}
