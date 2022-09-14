// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;

public class TransitionInstantiationCmd extends AbstractInstantiationCmd
{
    protected String transitionId;
    
    public TransitionInstantiationCmd(final String transitionId) {
        this(null, transitionId);
    }
    
    public TransitionInstantiationCmd(final String processInstanceId, final String transitionId) {
        this(processInstanceId, transitionId, null);
    }
    
    public TransitionInstantiationCmd(final String processInstanceId, final String transitionId, final String ancestorActivityInstanceId) {
        super(processInstanceId, ancestorActivityInstanceId);
        this.transitionId = transitionId;
    }
    
    @Override
    protected ScopeImpl getTargetFlowScope(final ProcessDefinitionImpl processDefinition) {
        final TransitionImpl transition = processDefinition.findTransition(this.transitionId);
        return transition.getSource().getFlowScope();
    }
    
    @Override
    protected CoreModelElement getTargetElement(final ProcessDefinitionImpl processDefinition) {
        final TransitionImpl transition = processDefinition.findTransition(this.transitionId);
        return transition;
    }
    
    public String getTargetElementId() {
        return this.transitionId;
    }
    
    @Override
    protected String describe() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Start transition '");
        sb.append(this.transitionId);
        sb.append("'");
        if (this.ancestorActivityInstanceId != null) {
            sb.append(" with ancestor activity instance '");
            sb.append(this.ancestorActivityInstanceId);
            sb.append("'");
        }
        return sb.toString();
    }
}
