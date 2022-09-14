// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.CleanableHistoricCaseInstanceReportResult;

public class CleanableHistoricCaseInstanceReportResultEntity implements CleanableHistoricCaseInstanceReportResult
{
    protected String caseDefinitionId;
    protected String caseDefinitionKey;
    protected String caseDefinitionName;
    protected int caseDefinitionVersion;
    protected Integer historyTimeToLive;
    protected long finishedCaseInstanceCount;
    protected long cleanableCaseInstanceCount;
    protected String tenantId;
    
    @Override
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public void setCaseDefinitionId(final String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }
    
    @Override
    public String getCaseDefinitionKey() {
        return this.caseDefinitionKey;
    }
    
    public void setCaseDefinitionKey(final String caseDefinitionKey) {
        this.caseDefinitionKey = caseDefinitionKey;
    }
    
    @Override
    public String getCaseDefinitionName() {
        return this.caseDefinitionName;
    }
    
    public void setCaseDefinitionName(final String caseDefinitionName) {
        this.caseDefinitionName = caseDefinitionName;
    }
    
    @Override
    public int getCaseDefinitionVersion() {
        return this.caseDefinitionVersion;
    }
    
    public void setCaseDefinitionVersion(final int caseDefinitionVersion) {
        this.caseDefinitionVersion = caseDefinitionVersion;
    }
    
    @Override
    public Integer getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }
    
    public void setHistoryTimeToLive(final Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
    
    @Override
    public long getFinishedCaseInstanceCount() {
        return this.finishedCaseInstanceCount;
    }
    
    public void setFinishedCaseInstanceCount(final Long finishedCaseInstanceCount) {
        this.finishedCaseInstanceCount = finishedCaseInstanceCount;
    }
    
    @Override
    public long getCleanableCaseInstanceCount() {
        return this.cleanableCaseInstanceCount;
    }
    
    public void setCleanableCaseInstanceCount(final Long cleanableCaseInstanceCount) {
        this.cleanableCaseInstanceCount = cleanableCaseInstanceCount;
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
        return this.getClass().getSimpleName() + "[caseDefinitionId = " + this.caseDefinitionId + ", caseDefinitionKey = " + this.caseDefinitionKey + ", caseDefinitionName = " + this.caseDefinitionName + ", caseDefinitionVersion = " + this.caseDefinitionVersion + ", historyTimeToLive = " + this.historyTimeToLive + ", finishedCaseInstanceCount = " + this.finishedCaseInstanceCount + ", cleanableCaseInstanceCount = " + this.cleanableCaseInstanceCount + ", tenantId = " + this.tenantId + "]";
    }
}
