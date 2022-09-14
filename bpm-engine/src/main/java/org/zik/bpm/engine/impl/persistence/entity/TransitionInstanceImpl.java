// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Arrays;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.runtime.TransitionInstance;

public class TransitionInstanceImpl extends ProcessElementInstanceImpl implements TransitionInstance
{
    protected String executionId;
    protected String activityId;
    protected String activityName;
    protected String activityType;
    protected String[] incidentIds;
    protected Incident[] incidents;
    
    public TransitionInstanceImpl() {
        this.incidentIds = TransitionInstanceImpl.NO_IDS;
        this.incidents = new Incident[0];
    }
    
    @Override
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    @Override
    public String getTargetActivityId() {
        return this.activityId;
    }
    
    @Override
    public String getExecutionId() {
        return this.executionId;
    }
    
    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
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
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[executionId=" + this.executionId + ", targetActivityId=" + this.activityId + ", activityName=" + this.activityName + ", activityType=" + this.activityType + ", id=" + this.id + ", parentActivityInstanceId=" + this.parentActivityInstanceId + ", processInstanceId=" + this.processInstanceId + ", processDefinitionId=" + this.processDefinitionId + ", incidentIds=" + Arrays.toString(this.incidentIds) + ", incidents=" + Arrays.toString(this.incidents) + "]";
    }
}
