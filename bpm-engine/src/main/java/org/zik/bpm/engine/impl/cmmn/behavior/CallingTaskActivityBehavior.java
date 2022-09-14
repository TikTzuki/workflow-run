// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;

public abstract class CallingTaskActivityBehavior extends TaskActivityBehavior
{
    protected static final CmmnBehaviorLogger LOG;
    protected BaseCallableElement callableElement;
    
    @Override
    public void onManualCompletion(final CmmnActivityExecution execution) {
        final String id = execution.getId();
        throw CallingTaskActivityBehavior.LOG.forbiddenManualCompletitionException("complete", id, this.getTypeName());
    }
    
    public BaseCallableElement getCallableElement() {
        return this.callableElement;
    }
    
    public void setCallableElement(final BaseCallableElement callableElement) {
        this.callableElement = callableElement;
    }
    
    protected String getDefinitionKey(final CmmnActivityExecution execution) {
        final CmmnExecution caseExecution = (CmmnExecution)execution;
        return this.getCallableElement().getDefinitionKey(caseExecution);
    }
    
    protected Integer getVersion(final CmmnActivityExecution execution) {
        final CmmnExecution caseExecution = (CmmnExecution)execution;
        return this.getCallableElement().getVersion(caseExecution);
    }
    
    protected String getDeploymentId(final CmmnActivityExecution execution) {
        return this.getCallableElement().getDeploymentId();
    }
    
    protected BaseCallableElement.CallableElementBinding getBinding() {
        return this.getCallableElement().getBinding();
    }
    
    protected boolean isLatestBinding() {
        return this.getCallableElement().isLatestBinding();
    }
    
    protected boolean isDeploymentBinding() {
        return this.getCallableElement().isDeploymentBinding();
    }
    
    protected boolean isVersionBinding() {
        return this.getCallableElement().isVersionBinding();
    }
    
    protected boolean isVersionTagBinding() {
        return this.getCallableElement().isVersionTagBinding();
    }
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
