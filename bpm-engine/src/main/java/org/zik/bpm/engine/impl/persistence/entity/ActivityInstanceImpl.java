// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.List;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.io.StringWriter;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.runtime.ActivityInstance;

public class ActivityInstanceImpl extends ProcessElementInstanceImpl implements ActivityInstance
{
    protected static final ActivityInstance[] NO_ACTIVITY_INSTANCES;
    protected static final TransitionInstance[] NO_TRANSITION_INSTANCES;
    protected String businessKey;
    protected String activityId;
    protected String activityName;
    protected String activityType;
    protected ActivityInstance[] childActivityInstances;
    protected TransitionInstance[] childTransitionInstances;
    protected String[] executionIds;
    protected String[] incidentIds;
    protected Incident[] incidents;
    
    public ActivityInstanceImpl() {
        this.childActivityInstances = ActivityInstanceImpl.NO_ACTIVITY_INSTANCES;
        this.childTransitionInstances = ActivityInstanceImpl.NO_TRANSITION_INSTANCES;
        this.executionIds = ActivityInstanceImpl.NO_IDS;
        this.incidentIds = ActivityInstanceImpl.NO_IDS;
        this.incidents = new Incident[0];
    }
    
    @Override
    public ActivityInstance[] getChildActivityInstances() {
        return this.childActivityInstances;
    }
    
    public void setChildActivityInstances(final ActivityInstance[] childInstances) {
        this.childActivityInstances = childInstances;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public void setBusinessKey(final String businessKey) {
        this.businessKey = businessKey;
    }
    
    @Override
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    @Override
    public String[] getExecutionIds() {
        return this.executionIds;
    }
    
    public void setExecutionIds(final String[] executionIds) {
        this.executionIds = executionIds;
    }
    
    @Override
    public TransitionInstance[] getChildTransitionInstances() {
        return this.childTransitionInstances;
    }
    
    public void setChildTransitionInstances(final TransitionInstance[] childTransitionInstances) {
        this.childTransitionInstances = childTransitionInstances;
    }
    
    @Override
    public String getActivityType() {
        return this.activityType;
    }
    
    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }
    
    @Override
    public String getActivityName() {
        return this.activityName;
    }
    
    public void setActivityName(final String activityName) {
        this.activityName = activityName;
    }
    
    @Override
    public String[] getIncidentIds() {
        return this.incidentIds;
    }
    
    public void setIncidentIds(final String[] incidentIds) {
        this.incidentIds = incidentIds;
    }
    
    @Override
    public Incident[] getIncidents() {
        return this.incidents;
    }
    
    public void setIncidents(final Incident[] incidents) {
        this.incidents = incidents;
    }
    
    protected void writeTree(final StringWriter writer, final String prefix, final boolean isTail) {
        writer.append(prefix);
        if (isTail) {
            writer.append("\u2514\u2500\u2500 ");
        }
        else {
            writer.append("\u251c\u2500\u2500 ");
        }
        writer.append(this.getActivityId() + "=>" + this.getId() + "\n");
        for (int i = 0; i < this.childTransitionInstances.length; ++i) {
            final TransitionInstance transitionInstance = this.childTransitionInstances[i];
            final boolean transitionIsTail = i == this.childTransitionInstances.length - 1 && this.childActivityInstances.length == 0;
            this.writeTransition(transitionInstance, writer, prefix + (isTail ? "    " : "\u2502   "), transitionIsTail);
        }
        for (int i = 0; i < this.childActivityInstances.length; ++i) {
            final ActivityInstanceImpl child = (ActivityInstanceImpl)this.childActivityInstances[i];
            child.writeTree(writer, prefix + (isTail ? "    " : "\u2502   "), i == this.childActivityInstances.length - 1);
        }
    }
    
    protected void writeTransition(final TransitionInstance transition, final StringWriter writer, final String prefix, final boolean isTail) {
        writer.append(prefix);
        if (isTail) {
            writer.append("\u2514\u2500\u2500 ");
        }
        else {
            writer.append("\u251c\u2500\u2500 ");
        }
        writer.append("transition to/from " + transition.getActivityId() + ":" + transition.getId() + "\n");
    }
    
    @Override
    public String toString() {
        final StringWriter writer = new StringWriter();
        this.writeTree(writer, "", true);
        return writer.toString();
    }
    
    @Override
    public ActivityInstance[] getActivityInstances(final String activityId) {
        EnsureUtil.ensureNotNull("activityId", (Object)activityId);
        final List<ActivityInstance> instances = new ArrayList<ActivityInstance>();
        this.collectActivityInstances(activityId, instances);
        return instances.toArray(new ActivityInstance[instances.size()]);
    }
    
    protected void collectActivityInstances(final String activityId, final List<ActivityInstance> instances) {
        if (this.activityId.equals(activityId)) {
            instances.add(this);
        }
        else {
            for (final ActivityInstance childInstance : this.childActivityInstances) {
                ((ActivityInstanceImpl)childInstance).collectActivityInstances(activityId, instances);
            }
        }
    }
    
    @Override
    public TransitionInstance[] getTransitionInstances(final String activityId) {
        EnsureUtil.ensureNotNull("activityId", (Object)activityId);
        final List<TransitionInstance> instances = new ArrayList<TransitionInstance>();
        this.collectTransitionInstances(activityId, instances);
        return instances.toArray(new TransitionInstance[instances.size()]);
    }
    
    protected void collectTransitionInstances(final String activityId, final List<TransitionInstance> instances) {
        boolean instanceFound = false;
        for (final TransitionInstance childTransitionInstance : this.childTransitionInstances) {
            if (activityId.equals(childTransitionInstance.getActivityId())) {
                instances.add(childTransitionInstance);
                instanceFound = true;
            }
        }
        if (!instanceFound) {
            for (final ActivityInstance childActivityInstance : this.childActivityInstances) {
                ((ActivityInstanceImpl)childActivityInstance).collectTransitionInstances(activityId, instances);
            }
        }
    }
    
    static {
        NO_ACTIVITY_INSTANCES = new ActivityInstance[0];
        NO_TRANSITION_INSTANCES = new TransitionInstance[0];
    }
}
