// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.core.model.CoreActivity;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.core.handler.HandlerContext;

public class CmmnHandlerContext implements HandlerContext
{
    protected ExpressionManager expressionManager;
    protected CmmnCaseDefinition caseDefinition;
    protected CmmnModelInstance model;
    protected CmmnActivity parent;
    protected Deployment deployment;
    
    public CmmnModelInstance getModel() {
        return this.model;
    }
    
    public void setModel(final CmmnModelInstance model) {
        this.model = model;
    }
    
    public CmmnCaseDefinition getCaseDefinition() {
        return this.caseDefinition;
    }
    
    public void setCaseDefinition(final CmmnCaseDefinition caseDefinition) {
        this.caseDefinition = caseDefinition;
    }
    
    @Override
    public CmmnActivity getParent() {
        return this.parent;
    }
    
    public void setParent(final CmmnActivity parent) {
        this.parent = parent;
    }
    
    public Deployment getDeployment() {
        return this.deployment;
    }
    
    public void setDeployment(final Deployment deployment) {
        this.deployment = deployment;
    }
    
    public ExpressionManager getExpressionManager() {
        return this.expressionManager;
    }
    
    public void setExpressionManager(final ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }
}
