// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.process;

import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmException;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityImpl extends ScopeImpl implements PvmActivity, HasDIBounds {
    private static final long serialVersionUID = 1L;
    protected List<PvmTransition> outgoingTransitions = new ArrayList();
    protected Map<String, TransitionImpl> namedOutgoingTransitions = new HashMap();
    protected List<PvmTransition> incomingTransitions = new ArrayList();
    protected ActivityBehavior activityBehavior;
    protected ActivityStartBehavior activityStartBehavior;
    protected ScopeImpl eventScope;
    protected ScopeImpl flowScope;
    protected boolean isScope;
    protected boolean isAsyncBefore;
    protected boolean isAsyncAfter;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected AsyncBeforeUpdate delegateAsyncBeforeUpdate;
    protected AsyncAfterUpdate delegateAsyncAfterUpdate;

    public ActivityImpl(String id, ProcessDefinitionImpl processDefinition) {
        super(id, processDefinition);
        this.activityStartBehavior = ActivityStartBehavior.DEFAULT;
        this.isScope = false;
        this.x = -1;
        this.y = -1;
        this.width = -1;
        this.height = -1;
    }

    public TransitionImpl createOutgoingTransition() {
        return this.createOutgoingTransition((String) null);
    }

    public TransitionImpl createOutgoingTransition(String transitionId) {
        TransitionImpl transition = new TransitionImpl(transitionId, this.processDefinition);
        transition.setSource(this);
        this.outgoingTransitions.add(transition);
        if (transitionId != null) {
            if (this.namedOutgoingTransitions.containsKey(transitionId)) {
                throw new PvmException("activity '" + this.id + " has duplicate transition '" + transitionId + "'");
            }

            this.namedOutgoingTransitions.put(transitionId, transition);
        }

        return transition;
    }

    public TransitionImpl findOutgoingTransition(String transitionId) {
        return (TransitionImpl) this.namedOutgoingTransitions.get(transitionId);
    }

    public String toString() {
        return "Activity(" + this.id + ")";
    }

    protected void setOutgoingTransitions(List<PvmTransition> outgoingTransitions) {
        this.outgoingTransitions = outgoingTransitions;
    }

    protected void setIncomingTransitions(List<PvmTransition> incomingTransitions) {
        this.incomingTransitions = incomingTransitions;
    }

    public List<PvmTransition> getOutgoingTransitions() {
        return this.outgoingTransitions;
    }

    public ActivityBehavior getActivityBehavior() {
        return this.activityBehavior;
    }

    public void setActivityBehavior(ActivityBehavior activityBehavior) {
        this.activityBehavior = activityBehavior;
    }

    public ActivityStartBehavior getActivityStartBehavior() {
        return this.activityStartBehavior;
    }

    public void setActivityStartBehavior(ActivityStartBehavior activityStartBehavior) {
        this.activityStartBehavior = activityStartBehavior;
    }

    public List<PvmTransition> getIncomingTransitions() {
        return this.incomingTransitions;
    }

    public boolean isScope() {
        return this.isScope;
    }

    public void setScope(boolean isScope) {
        this.isScope = isScope;
    }

    public boolean isAsyncBefore() {
        return this.isAsyncBefore;
    }

    public void setAsyncBefore(boolean isAsyncBefore) {
        this.setAsyncBefore(isAsyncBefore, true);
    }

    public void setAsyncBefore(boolean isAsyncBefore, boolean exclusive) {
        if (this.delegateAsyncBeforeUpdate != null) {
            this.delegateAsyncBeforeUpdate.updateAsyncBefore(isAsyncBefore, exclusive);
        }

        this.isAsyncBefore = isAsyncBefore;
    }

    public boolean isAsyncAfter() {
        return this.isAsyncAfter;
    }

    public void setAsyncAfter(boolean isAsyncAfter) {
        this.setAsyncAfter(isAsyncAfter, true);
    }

    public void setAsyncAfter(boolean isAsyncAfter, boolean exclusive) {
        if (this.delegateAsyncAfterUpdate != null) {
            this.delegateAsyncAfterUpdate.updateAsyncAfter(isAsyncAfter, exclusive);
        }

        this.isAsyncAfter = isAsyncAfter;
    }

    public String getActivityId() {
        return super.getId();
    }

    public ScopeImpl getFlowScope() {
        return this.flowScope;
    }

    public ScopeImpl getEventScope() {
        return this.eventScope;
    }

    public void setEventScope(ScopeImpl eventScope) {
        if (this.eventScope != null) {
            this.eventScope.eventActivities.remove(this);
        }

        this.eventScope = eventScope;
        if (eventScope != null) {
            this.eventScope.eventActivities.add(this);
        }

    }

    public PvmScope getLevelOfSubprocessScope() {
        ScopeImpl levelOfSubprocessScope;
        for (levelOfSubprocessScope = this.getFlowScope(); !levelOfSubprocessScope.isSubProcessScope; levelOfSubprocessScope = ((PvmActivity) levelOfSubprocessScope).getFlowScope()) {
        }

        return levelOfSubprocessScope;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ActivityImpl getParentFlowScopeActivity() {
        ScopeImpl flowScope = this.getFlowScope();
        return flowScope != this.getProcessDefinition() ? (ActivityImpl) flowScope : null;
    }

    public boolean isCompensationHandler() {
        Boolean isForCompensation = (Boolean) this.getProperty("isForCompensation");
        return Boolean.TRUE.equals(isForCompensation);
    }

    public ActivityImpl findCompensationHandler() {
        String compensationHandlerId = (String) this.getProperty("compensationHandler");
        return compensationHandlerId != null ? this.getProcessDefinition().findActivity(compensationHandlerId) : null;
    }

    public boolean isMultiInstance() {
        Boolean isMultiInstance = (Boolean) this.getProperty("isMultiInstance");
        return Boolean.TRUE.equals(isMultiInstance);
    }

    public boolean isTriggeredByEvent() {
        Boolean isTriggeredByEvent = (Boolean) this.getProperties().get(BpmnProperties.TRIGGERED_BY_EVENT);
        return Boolean.TRUE.equals(isTriggeredByEvent);
    }

    public AsyncBeforeUpdate getDelegateAsyncBeforeUpdate() {
        return this.delegateAsyncBeforeUpdate;
    }

    public void setDelegateAsyncBeforeUpdate(AsyncBeforeUpdate delegateAsyncBeforeUpdate) {
        this.delegateAsyncBeforeUpdate = delegateAsyncBeforeUpdate;
    }

    public AsyncAfterUpdate getDelegateAsyncAfterUpdate() {
        return this.delegateAsyncAfterUpdate;
    }

    public void setDelegateAsyncAfterUpdate(AsyncAfterUpdate delegateAsyncAfterUpdate) {
        this.delegateAsyncAfterUpdate = delegateAsyncAfterUpdate;
    }

    public interface AsyncAfterUpdate {
        void updateAsyncAfter(boolean var1, boolean var2);
    }

    public interface AsyncBeforeUpdate {
        void updateAsyncBefore(boolean var1, boolean var2);
    }
}
