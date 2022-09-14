// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;

public class ActivityAfterInstantiationCmd extends AbstractInstantiationCmd
{
    protected String activityId;
    
    public ActivityAfterInstantiationCmd(final String activityId) {
        this(null, activityId);
    }
    
    public ActivityAfterInstantiationCmd(final String processInstanceId, final String activityId) {
        this(processInstanceId, activityId, null);
    }
    
    public ActivityAfterInstantiationCmd(final String processInstanceId, final String activityId, final String ancestorActivityInstanceId) {
        super(processInstanceId, ancestorActivityInstanceId);
        this.activityId = activityId;
    }
    
    @Override
    protected ScopeImpl getTargetFlowScope(final ProcessDefinitionImpl processDefinition) {
        final TransitionImpl transition = this.findTransition(processDefinition);
        return transition.getDestination().getFlowScope();
    }
    
    @Override
    protected CoreModelElement getTargetElement(final ProcessDefinitionImpl processDefinition) {
        return this.findTransition(processDefinition);
    }
    
    protected TransitionImpl findTransition(final ProcessDefinitionImpl processDefinition) {
        final PvmActivity activity = processDefinition.findActivity(this.activityId);
        EnsureUtil.ensureNotNull(NotValidException.class, this.describeFailure("Activity '" + this.activityId + "' does not exist"), "activity", activity);
        if (activity.getOutgoingTransitions().isEmpty()) {
            throw new ProcessEngineException("Cannot start after activity " + this.activityId + "; activity has no outgoing sequence flow to take");
        }
        if (activity.getOutgoingTransitions().size() > 1) {
            throw new ProcessEngineException("Cannot start after activity " + this.activityId + "; activity has more than one outgoing sequence flow");
        }
        return activity.getOutgoingTransitions().get(0);
    }
    
    public String getTargetElementId() {
        return this.activityId;
    }
    
    @Override
    protected String describe() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Start after activity '");
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
