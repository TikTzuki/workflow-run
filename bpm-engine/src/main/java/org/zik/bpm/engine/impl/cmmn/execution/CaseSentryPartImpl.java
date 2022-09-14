// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.execution;

public class CaseSentryPartImpl extends CmmnSentryPart
{
    private static final long serialVersionUID = 1L;
    protected CaseExecutionImpl caseInstance;
    protected CaseExecutionImpl caseExecution;
    protected CaseExecutionImpl sourceCaseExecution;
    
    @Override
    public CaseExecutionImpl getCaseInstance() {
        return this.caseInstance;
    }
    
    @Override
    public void setCaseInstance(final CmmnExecution caseInstance) {
        this.caseInstance = (CaseExecutionImpl)caseInstance;
    }
    
    @Override
    public CmmnExecution getCaseExecution() {
        return this.caseExecution;
    }
    
    @Override
    public void setCaseExecution(final CmmnExecution caseExecution) {
        this.caseExecution = (CaseExecutionImpl)caseExecution;
    }
    
    @Override
    public CmmnExecution getSourceCaseExecution() {
        return this.sourceCaseExecution;
    }
    
    @Override
    public void setSourceCaseExecution(final CmmnExecution sourceCaseExecution) {
        this.sourceCaseExecution = (CaseExecutionImpl)sourceCaseExecution;
    }
    
    public String getId() {
        return String.valueOf(System.identityHashCode(this));
    }
    
    public String getCaseInstanceId() {
        if (this.caseInstance != null) {
            return this.caseInstance.getId();
        }
        return null;
    }
    
    public String getCaseExecutionId() {
        if (this.caseExecution != null) {
            return this.caseExecution.getId();
        }
        return null;
    }
    
    @Override
    public String getSourceCaseExecutionId() {
        if (this.sourceCaseExecution != null) {
            return this.sourceCaseExecution.getId();
        }
        return null;
    }
}
