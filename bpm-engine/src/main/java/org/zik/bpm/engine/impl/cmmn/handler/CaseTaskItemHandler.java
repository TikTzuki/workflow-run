// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CaseTask;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.zik.bpm.engine.impl.cmmn.behavior.CaseTaskActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;

public class CaseTaskItemHandler extends ProcessOrCaseTaskItemHandler
{
    @Override
    protected CmmnActivityBehavior getActivityBehavior() {
        return new CaseTaskActivityBehavior();
    }
    
    protected CaseTask getDefinition(final CmmnElement element) {
        return (CaseTask)super.getDefinition(element);
    }
    
    @Override
    protected String getDefinitionKey(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final CaseTask definition = this.getDefinition(element);
        return definition.getCase();
    }
    
    @Override
    protected String getBinding(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final CaseTask definition = this.getDefinition(element);
        return definition.getCamundaCaseBinding();
    }
    
    @Override
    protected String getVersion(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final CaseTask definition = this.getDefinition(element);
        return definition.getCamundaCaseVersion();
    }
    
    @Override
    protected String getTenantId(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final CaseTask definition = this.getDefinition(element);
        return definition.getCamundaCaseTenantId();
    }
}
