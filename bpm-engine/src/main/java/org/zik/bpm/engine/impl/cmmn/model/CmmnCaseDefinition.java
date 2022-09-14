// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.model;

import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionImpl;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnCaseInstance;

public class CmmnCaseDefinition extends CmmnActivity
{
    private static final long serialVersionUID = 1L;
    
    public CmmnCaseDefinition(final String id) {
        super(id, null);
        this.caseDefinition = this;
    }
    
    public CmmnCaseInstance createCaseInstance() {
        return this.createCaseInstance(null);
    }
    
    public CmmnCaseInstance createCaseInstance(final String businessKey) {
        final CmmnExecution caseInstance = this.newCaseInstance();
        caseInstance.setCaseDefinition(this);
        caseInstance.setCaseInstance(caseInstance);
        caseInstance.setBusinessKey(businessKey);
        final CmmnActivity casePlanModel = this.getActivities().get(0);
        caseInstance.setActivity(casePlanModel);
        return caseInstance;
    }
    
    protected CmmnExecution newCaseInstance() {
        return new CaseExecutionImpl();
    }
}
