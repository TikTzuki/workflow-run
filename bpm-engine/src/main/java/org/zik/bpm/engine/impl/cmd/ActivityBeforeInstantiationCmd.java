// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class ActivityBeforeInstantiationCmd extends AbstractInstantiationCmd
{
    protected String activityId;
    
    public ActivityBeforeInstantiationCmd(final String activityId) {
        this(null, activityId);
    }
    
    public ActivityBeforeInstantiationCmd(final String processInstanceId, final String activityId) {
        this(processInstanceId, activityId, null);
    }
    
    public ActivityBeforeInstantiationCmd(final String processInstanceId, final String activityId, final String ancestorActivityInstanceId) {
        super(processInstanceId, ancestorActivityInstanceId);
        this.activityId = activityId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final ExecutionEntity processInstance = commandContext.getExecutionManager().findExecutionById(this.processInstanceId);
        final ProcessDefinitionImpl processDefinition = processInstance.getProcessDefinition();
        final PvmActivity activity = processDefinition.findActivity(this.activityId);
        if (activity != null && "compensationBoundaryCatch".equals(activity.getProperty("type"))) {
            throw new ProcessEngineException("Cannot start before activity " + this.activityId + "; activity is a compensation boundary event.");
        }
        return super.execute(commandContext);
    }
    
    @Override
    protected ScopeImpl getTargetFlowScope(final ProcessDefinitionImpl processDefinition) {
        final PvmActivity activity = processDefinition.findActivity(this.activityId);
        return activity.getFlowScope();
    }
    
    @Override
    protected CoreModelElement getTargetElement(final ProcessDefinitionImpl processDefinition) {
        final ActivityImpl activity = processDefinition.findActivity(this.activityId);
        return activity;
    }
    
    public String getTargetElementId() {
        return this.activityId;
    }
    
    @Override
    protected String describe() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Start before activity '");
        sb.append(this.activityId);
        sb.append("'");
        if (this.ancestorActivityInstanceId != null) {
            sb.append(" with ancestor activity instance '");
            sb.append(this.ancestorActivityInstanceId);
            sb.append("'");
        }
        return sb.toString();
    }
}
