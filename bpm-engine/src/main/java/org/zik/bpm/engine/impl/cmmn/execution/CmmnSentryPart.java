// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.execution;

import java.io.Serializable;

public abstract class CmmnSentryPart implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String type;
    protected String sentryId;
    protected String standardEvent;
    protected String source;
    protected String variableEvent;
    protected String variableName;
    protected boolean satisfied;
    
    public CmmnSentryPart() {
        this.satisfied = false;
    }
    
    public abstract CmmnExecution getCaseInstance();
    
    public abstract void setCaseInstance(final CmmnExecution p0);
    
    public abstract CmmnExecution getCaseExecution();
    
    public abstract void setCaseExecution(final CmmnExecution p0);
    
    public String getSentryId() {
        return this.sentryId;
    }
    
    public void setSentryId(final String sentryId) {
        this.sentryId = sentryId;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void setSource(final String source) {
        this.source = source;
    }
    
    @Deprecated
    public abstract String getSourceCaseExecutionId();
    
    @Deprecated
    public abstract CmmnExecution getSourceCaseExecution();
    
    @Deprecated
    public abstract void setSourceCaseExecution(final CmmnExecution p0);
    
    public String getStandardEvent() {
        return this.standardEvent;
    }
    
    public void setStandardEvent(final String standardEvent) {
        this.standardEvent = standardEvent;
    }
    
    public boolean isSatisfied() {
        return this.satisfied;
    }
    
    public void setSatisfied(final boolean satisfied) {
        this.satisfied = satisfied;
    }
    
    public String getVariableEvent() {
        return this.variableEvent;
    }
    
    public void setVariableEvent(final String variableEvent) {
        this.variableEvent = variableEvent;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public void setVariableName(final String variableName) {
        this.variableName = variableName;
    }
}
