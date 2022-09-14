// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.ProcessTask;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.zik.bpm.engine.impl.cmmn.behavior.ProcessTaskActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;

public class ProcessTaskItemHandler extends ProcessOrCaseTaskItemHandler
{
    @Override
    protected CmmnActivityBehavior getActivityBehavior() {
        return new ProcessTaskActivityBehavior();
    }
    
    protected ProcessTask getDefinition(final CmmnElement element) {
        return (ProcessTask)super.getDefinition(element);
    }
    
    @Override
    protected String getDefinitionKey(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final ProcessTask definition = this.getDefinition(element);
        return definition.getProcess();
    }
    
    @Override
    protected String getBinding(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final ProcessTask definition = this.getDefinition(element);
        return definition.getCamundaProcessBinding();
    }
    
    @Override
    protected String getVersion(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final ProcessTask definition = this.getDefinition(element);
        return definition.getCamundaProcessVersion();
    }
    
    @Override
    protected String getTenantId(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final ProcessTask definition = this.getDefinition(element);
        return definition.getCamundaProcessTenantId();
    }
}
